module jmp.cloud.service.impl {
    requires transitive jmp.service.api;
    requires jmp.dto;

    provides com.epam.jmp.service.Service with com.epam.jmp.impl.ServiceImpl;
    exports com.epam.jmp.impl;
}
