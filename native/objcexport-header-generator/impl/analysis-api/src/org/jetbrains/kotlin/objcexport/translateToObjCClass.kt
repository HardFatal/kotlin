package org.jetbrains.kotlin.objcexport

import org.jetbrains.kotlin.analysis.api.KtAnalysisSession
import org.jetbrains.kotlin.analysis.api.symbols.*
import org.jetbrains.kotlin.analysis.api.symbols.markers.KtSymbolWithModality
import org.jetbrains.kotlin.analysis.api.types.KtNonErrorClassType
import org.jetbrains.kotlin.backend.konan.objcexport.*
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.objcexport.analysisApiUtils.isThrowable
import org.jetbrains.kotlin.objcexport.analysisApiUtils.isVisibleInObjC

context(KtAnalysisSession, KtObjCExportSession)
fun KtClassOrObjectSymbol.translateToObjCClass(): ObjCClass? {
    require(classKind == KtClassKind.CLASS || classKind == KtClassKind.ENUM_CLASS)
    if (!isVisibleInObjC()) return null

    val enumKind = this.classKind == KtClassKind.ENUM_CLASS
    val final = if (this is KtSymbolWithModality) this.modality == Modality.FINAL else false

    val name = getObjCClassOrProtocolName()
    val attributes = (if (enumKind || final) listOf(OBJC_SUBCLASSING_RESTRICTED) else emptyList()) + name.toNameAttributes()

    val comment: ObjCComment? = annotations.translateToObjCComment()
    val origin = getObjCExportStubOrigin()

    val superClass = translateSuperClass()
    val superProtocols: List<String> = superProtocols()

    val members = buildList<ObjCExportStub> {
        /* The order of members tries to replicate the K1 implementation explicitly */
        this += translateToObjCConstructors()

        if (needsCompanionProperty) {
            this += buildCompanionProperty()
        }

        this += getCallableSymbolsForObjCMemberTranslation()
            .sortedWith(StableCallableOrder)
            .flatMap { it.translateToObjCExportStub() }

        if (classKind == KtClassKind.ENUM_CLASS) {
            this += translateEnumMembers()
        }

        if (isThrowable) {
            this += buildThrowableAsErrorMethod()
        }
    }

    val categoryName: String? = null

    val generics: List<ObjCGenericTypeDeclaration> = typeParameters.map { typeParameter ->
        ObjCGenericTypeParameterDeclaration(
            typeParameter.nameOrAnonymous.asString().toValidObjCSwiftIdentifier(),
            ObjCVariance.fromKotlinVariance(typeParameter.variance)
        )
    }

    return ObjCInterfaceImpl(
        name = name.objCName,
        comment = comment,
        origin = origin,
        attributes = attributes,
        superProtocols = superProtocols,
        members = members,
        categoryName = categoryName,
        generics = generics,
        superClass = superClass.superClassName.objCName,
        superClassGenerics = superClass.superClassGenerics
    )
}

/**
 * Resolves all [KtCallableSymbol] symbols that are to be translated to ObjC for [this] [KtClassOrObjectSymbol].
 * Note: This will return only 'declared' members (aka members written on this class/interface/object) and 'synthetic'/'generated' members.
 *
 * ## Example regular class
 * ```kotlin
 * open class Base {
 *     fun base() = Unit
 * }
 *
 * class Foo : Base() {
 *     fun foo() = Unit
 * ```
 *
 * In this example `Foo` will return the function `foo` (as declared in `Foo`), but not the function `base` (as declared in `Base` and
 * not directly in `Foo`).
 *
 * ## Example data class
 * ```kotlin
 * data class Foo(val x: Int)
 * ```
 *
 * Will return `x` as directly declared in `Foo`, but also the `copy`, `equals`,`hashCode`, `toString` and `componentX` functions that
 * are generated by the compiler for the *data* class `Foo`
 *
 * Note: Some methods like `hashCode`, `toString`, ... have predefined selectors and ObjC names.
 * @see [Predefined]
 */
context(KtAnalysisSession)
internal fun KtClassOrObjectSymbol.getCallableSymbolsForObjCMemberTranslation(): Set<KtCallableSymbol> {
    val generatedCallableSymbols = getMemberScope()
        .callables()
        .filter { it.origin == KtSymbolOrigin.SOURCE_MEMBER_GENERATED }
        .toSet()

    val declaredCallableSymbols = getDeclaredMemberScope()
        .callables()
        .toSet()

    return generatedCallableSymbols + declaredCallableSymbols
}

context(KtAnalysisSession, KtObjCExportSession)
internal fun KtNonErrorClassType.getSuperClassName(): ObjCExportClassOrProtocolName? {
    val symbol = expandedSymbol ?: return null
    return symbol.getObjCClassOrProtocolName()
}
