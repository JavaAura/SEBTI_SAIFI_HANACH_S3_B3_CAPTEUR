package com.capt.capteurs.mapper;

import com.capt.capteurs.dto.request.DeviceRequestDTO;
import com.capt.capteurs.dto.response.DeviceResponseDTO;
import com.capt.capteurs.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface Devicemapper {

    Device toDevice(DeviceRequestDTO deviceRequestDTO);

    @Mapping(source = "device.zone.name", target = "name_zone")
    DeviceResponseDTO toDeviceResponseDTO(Device device);
}
