cmake_minimum_required(VERSION 3.27)
project(injector)

option(BUILD_STATIC_LIBS "Build static libraries" OFF)
set(CMAKE_CXX_STANDARD 17)
SET(EXECUTABLE_OUTPUT_PATH  ${PROJECT_BINARY_DIR}/../../fish)
SET(LIBRARY_OUTPUT_PATH  ${PROJECT_BINARY_DIR}/../../fish)





add_library(injector
            SHARED
        src/injector/Inject.c
        src/injector/GetProcAddressR.c
        src/injector/LoadLibraryR.c
)


add_library(agent
        SHARED
        src/dll/utils.c
        src/dll/dllmain.c
        src/dll/ReflectiveLoader.c
)




add_executable(launcher
                src/launcher/main.c
)
target_link_libraries(launcher "user32.lib")






