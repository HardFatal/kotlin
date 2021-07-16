/*
 * Copyright 2010-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

#include "Exceptions.h"

#include <csignal>
#include <memory>
#include <future>

#include "gmock/gmock.h"
#include "gtest/gtest.h"

#include "Memory.h"
#include "TestSupport.hpp"
#include "TestSupportCompilerGenerated.hpp"

using namespace kotlin;
using namespace testing;

namespace {

using NativeHandlerMock = testing::StrictMock<testing::MockFunction<void(void)>>;
using OnUnhandledExceptionMock = testing::StrictMock<testing::MockFunction<void(KRef)>>;

// TODO: Use ScopedStrictMock
KStdUniquePtr<NativeHandlerMock> gNativeHandlerMock = nullptr;
KStdUniquePtr<ScopedStrictMockFunction<void(KRef)>> gOnUnhandledExceptionMock = nullptr;

constexpr int kExpectedExitCode = 42;

NativeHandlerMock& setNativeTerminateHandler() noexcept {
    gNativeHandlerMock = make_unique<NativeHandlerMock>();
    std::set_terminate([]() {
        gNativeHandlerMock->Call();
        std::exit(kExpectedExitCode);
    });
    // TODO: May be get rid of exit?
    std::atexit([]() {
        // Clear the mock. TODO: extend the comment
        gNativeHandlerMock.reset(nullptr);

    });
    return *gNativeHandlerMock;
}

OnUnhandledExceptionMock& setKotlinTerminationHandler() noexcept {
    gOnUnhandledExceptionMock = make_unique<ScopedStrictMockFunction<void(KRef)>>(ScopedOnUnhandledExceptionMock());
    SetKonanTerminateHandler();
    std::atexit([]() {
        // Clear the mock. TODO: extend the comment.
        gOnUnhandledExceptionMock.reset(nullptr);
    });
    // Kotlin's handler calls konan::abort() in case of unhandled Kotlin exception.
    std::signal(SIGABRT, [](int sig) {
       std::exit(kExpectedExitCode);
    });
    return gOnUnhandledExceptionMock->get();
}
    
void loggingAssert(bool condition, const char* message) noexcept {
    if (!condition) {
        std::cerr << "FAIL: " << message << std::endl;
    }
}

void log(const char* message) noexcept {
    std::cerr << message << std::endl;
}

void setupMocks() noexcept {
    auto& nativeHandlerMock = setNativeTerminateHandler();
    Mock::AllowLeak(&nativeHandlerMock);
    EXPECT_CALL(nativeHandlerMock, Call)
            .WillOnce([]() {
                loggingAssert(GetThreadState() == ThreadState::kNative, "Expected kNative thread state in the native handler");
                log("Native handler");
            });

    auto& onUnhandledExceptionMock = setKotlinTerminationHandler();
    Mock::AllowLeak(&onUnhandledExceptionMock);
    EXPECT_CALL(onUnhandledExceptionMock, Call)
            .WillOnce([]() {
                loggingAssert(GetThreadState() == ThreadState::kRunnable, "Expected kRunnable state in the Kotlin handler");
                log("Kotlin handler");
            });
}

} // namespace

#define EXPERIMENTAL_MM_ONLY()                                          \
    do {                                                              \
        if (CurrentMemoryModel != MemoryModel::kExperimental) {       \
            GTEST_SKIP() << "This test requires the Experimental MM"; \
        } \
    } while(false)

#define ASSERTS_PASSED AllOf(Not(HasSubstr("FAIL")), Not(HasSubstr("Unexpected thread state")))
#define KOTLIN_HANDLER_RAN HasSubstr("Kotlin handler")
#define NATIVE_HANDLER_RAN HasSubstr("Native handler")

// TODO: Comment not using mock machinery

// TODO: Rework mocks.

// Move out seting up mocks

TEST(TerminationHandlerDeathTest, TerminationInRunnableState) {
    EXPERIMENTAL_MM_ONLY();
    auto testBlock = []() {
        setupMocks();

        ScopedMemoryInit init;
        loggingAssert(GetThreadState() == ThreadState::kRunnable, "Expected kRunnable thread state before std::terminate");
        std::terminate();
    };

    EXPECT_EXIT(testBlock(),
                testing::ExitedWithCode(kExpectedExitCode),
                AllOf(ASSERTS_PASSED, NATIVE_HANDLER_RAN, Not(KOTLIN_HANDLER_RAN)));
}

TEST(TerminationHandlerDeathTest, TerminationInNativeState) {
    EXPERIMENTAL_MM_ONLY();
    auto testBlock = []() {
        setupMocks();

        ScopedMemoryInit init;
        ThreadStateGuard stateGuard(ThreadState::kNative);
        loggingAssert(GetThreadState() == ThreadState::kNative, "Expected native thread state before std::terminate");
        std::terminate();
    };

    EXPECT_EXIT(testBlock(),
                testing::ExitedWithCode(kExpectedExitCode),
                AllOf(ASSERTS_PASSED, NATIVE_HANDLER_RAN, Not(KOTLIN_HANDLER_RAN)));
}

TEST(TerminationHandlerDeathTest, TerminationInForeignThread) {
    EXPERIMENTAL_MM_ONLY();
    auto testBlock = []() {
        setupMocks();

        loggingAssert(mm::GetMemoryState() == nullptr, "Expected unregistered thread before std::terminate");
        std::terminate();
    };

    EXPECT_EXIT(testBlock(),
                testing::ExitedWithCode(kExpectedExitCode),
                AllOf(ASSERTS_PASSED, NATIVE_HANDLER_RAN, Not(KOTLIN_HANDLER_RAN)));
}

TEST(TerminationHandlerDeathTest, UnhandledKotlinExceptionInRunnableState) {
    EXPERIMENTAL_MM_ONLY();
    auto testBlock = []() {
        setupMocks();

        RunInNewThread([](MemoryState* thread) {
            loggingAssert(GetThreadState(thread) == ThreadState::kRunnable, "Expected kRunanble thread state before throwing");
            ObjHeader exception{};
            ExceptionObjHolder::Throw(&exception);
        });
    };

    EXPECT_EXIT(testBlock(),
                testing::ExitedWithCode(kExpectedExitCode),
                AllOf(ASSERTS_PASSED, KOTLIN_HANDLER_RAN, Not(NATIVE_HANDLER_RAN)));
}

TEST(TerminationHandlerDeathTest, UnhandledKotlinExceptionInNativeState) {
    EXPERIMENTAL_MM_ONLY();
    auto testBlock = []() {
        setupMocks();

        // It is possible if a Kotlin exception thrown by a Kotlin callback is re-thrown in
        // another thread which is attached to the Kotlin runtime but has the kNative state.
        RunInNewThread([](MemoryState* thread) {
            SwitchThreadState(thread, kotlin::ThreadState::kNative);
            loggingAssert(GetThreadState(thread) == ThreadState::kNative, "Expected kNative thread state before throwing");
            ObjHeader exception{};
            ExceptionObjHolder::Throw(&exception);
        });
    };

    EXPECT_EXIT(testBlock(),
                testing::ExitedWithCode(kExpectedExitCode),
                AllOf(ASSERTS_PASSED, KOTLIN_HANDLER_RAN, Not(NATIVE_HANDLER_RAN)));
}

TEST(TerminationHandlerDeathTest, UnhandledKotlinExceptionInForeignThread) {
    EXPERIMENTAL_MM_ONLY();
    auto testBlock = []() {
        setupMocks();

        // It is possible if a Kotlin exception thrown by a Kotlin callback is re-thrown in
        // another thread which is not attached to the Kotlin runtime at all.
        std::thread foreignThread([]() {
            loggingAssert(mm::GetMemoryState() == nullptr, "Expected unregistered thread before throwing");

            auto future = std::async(std::launch::async, []() {
                // Initial Kotlin exception throwing requires initialized runtime.
                ScopedMemoryInit init;
                ObjHeader exception{};
                ExceptionObjHolder::Throw(&exception);
            });
            // Re-throw the Kotlin exception in a foreign thread.
            future.get();
        });
        foreignThread.join();
    };

    EXPECT_EXIT(testBlock(),
                testing::ExitedWithCode(kExpectedExitCode),
                AllOf(ASSERTS_PASSED, KOTLIN_HANDLER_RAN, Not(NATIVE_HANDLER_RAN)));
}

TEST(TerminationHandlerDeathTest, UnhandledForeignExceptionInNativeState) {
    EXPERIMENTAL_MM_ONLY();
    auto testBlock = []() {
        setupMocks();

        RunInNewThread([](MemoryState* thread) {
            SwitchThreadState(thread, ThreadState::kNative);
            loggingAssert(GetThreadState(thread) == ThreadState::kNative, "Expected kNative thread state before throwing");

            throw std::runtime_error("Foreign exception");
        });
    };

    EXPECT_EXIT(testBlock(),
                testing::ExitedWithCode(kExpectedExitCode),
                AllOf(ASSERTS_PASSED, NATIVE_HANDLER_RAN, Not(KOTLIN_HANDLER_RAN)));
}

TEST(TerminationHandlerDeathTest, UnhandledForeignExceptionInForeignThread) {
    EXPERIMENTAL_MM_ONLY();
    auto testBlock = []() {
        setupMocks();

        std::thread foreignThread([]() {
            loggingAssert(mm::GetMemoryState() == nullptr, "Expected unregistered thread before throwing");
            throw std::runtime_error("Foreign exception");
        });
        foreignThread.join();
    };

    EXPECT_EXIT(testBlock(),
                testing::ExitedWithCode(kExpectedExitCode),
                AllOf(ASSERTS_PASSED, NATIVE_HANDLER_RAN, Not(KOTLIN_HANDLER_RAN)));
}

// Model a filtering exception handler which terminates the program if an interop call throws a foreign exception.
TEST(TerminationHandlerDeathTest, TerminationInForeignExceptionCatch) {
    EXPERIMENTAL_MM_ONLY();
    auto testBlock = []() {
        setupMocks();

        ScopedMemoryInit init;
        loggingAssert(GetThreadState(init.memoryState()) == ThreadState::kRunnable, "Expected kRunnable state before catching");

        try {
            throw std::runtime_error("Foreign exception");
        } catch(...) {
            std::terminate();
        }
    };

    EXPECT_EXIT(testBlock(),
                testing::ExitedWithCode(kExpectedExitCode),
                AllOf(ASSERTS_PASSED, NATIVE_HANDLER_RAN, Not(KOTLIN_HANDLER_RAN)));
}