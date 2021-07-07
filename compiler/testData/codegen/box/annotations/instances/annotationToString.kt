// IGNORE_BACKEND_FIR: JVM_IR
// TARGET_BACKEND: JVM_IR

// WITH_RUNTIME
// !LANGUAGE: +InstantiationOfAnnotationClasses

package a

import kotlin.reflect.KClass

enum class E { A, B }

annotation class Bar(val i:Int, val s: String, val e: E, val iA: IntArray)

annotation class Foo(
    val int: Int,
    val s: String,
    val arr: Array<String>,
    val bar: Bar
)

fun box(): String {
    val foo1 = Foo(42, "foo", arrayOf("a", "b"), Bar(10, "bar", E.B, intArrayOf(1, 2)))
    val foo2 = Foo(42, "foo", arrayOf("a", "b"), Bar(10, "bar", E.B, intArrayOf(1, 2)))
    val s1 = foo1.toString()
    val s2 = foo2.toString()
    if (s1 != s2) return "FAIL"
    if (s1 != "@a.Foo(int=42, s=foo, arr=[a, b], bar=@a.Bar(i=10, s=bar, e=B, iA=[1, 2]))") return "FAIL $s1"
    return "OK"
}