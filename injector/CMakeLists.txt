cmake_minimum_required(VERSION 3.27)
project(injector)


set(CMAKE_CXX_STANDARD 17)
SET(EXECUTABLE_OUTPUT_PATH ${PROJECT_BINARY_DIR}/bin)
SET(LIBRARY_OUTPUT_PATH  ${PROJECT_BINARY_DIR}/bin)





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
        src/hook/hook.c
)




add_executable(launcher
                src/launcher/main.c
)






