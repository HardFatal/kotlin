/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

#include "testlib_api.h"

#include <iostream>
#include <exception>
#include <future>

int main(int argc, char** argv) {
    // Run Kotlin code in a separate thread and then get the exception in the
    // main thread which is not registered in the Kotlin runtime.
    std::cout << "main thread: " <<  std::this_thread::get_id() << std::endl;
    std::async(std::launch::async, []() {
        std::cout << "async thread: " <<  std::this_thread::get_id() << std::endl;
        testlib_symbols()->kotlin.root.setHookAndThrow();
    }).get();
}