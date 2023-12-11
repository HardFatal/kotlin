// ISSUE: KT-63076

// FILE: A.kt
open class A {
    open fun getX1(): String = ""
    open fun getX2(): String = ""
    open fun getX3(): String = ""
    open fun getX4(): String = ""
}

// FILE: B.java
public class B extends A {
    public String getX2() { return ""; }
    public String getX3() { return ""; }
    public String getX4() { return ""; }
}

// FILE: C.kt
open class C : B() {
    override fun getX3(): String = ""
    override fun getX4(): String = ""
}

// FILE: D.java
public class D extends C {
    public String getX4() { return ""; }
}

// FILE: main.kt

fun test_1(a: A) {
    a.<!UNRESOLVED_REFERENCE!>x1<!>
    a.<!UNRESOLVED_REFERENCE!>x2<!>
    a.<!UNRESOLVED_REFERENCE!>x3<!>
    a.<!UNRESOLVED_REFERENCE!>x4<!>
}

fun test_2(b: B) {
    b.x1
    b.x2
    b.x3
    b.x4
}

fun test_3(c: C) {
    c.x1
    c.x2
    c.x3
    c.x4
}

fun test_4(d: D) {
    d.x1
    d.x2
    d.x3
    d.x4
}
