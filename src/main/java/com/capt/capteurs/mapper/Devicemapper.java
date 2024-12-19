package com.capt.capteurs.mapper;

import com.capt.capteurs.dto.request.DeviceRequestDTO;
import com.capt.capteurs.dto.response.DeviceResponseDTO;
import com.capt.capteurs.model.Device;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface Devicemapper {

    Device toDevice(DeviceRequestDTO deviceRequestDTO);

    DeviceResponseDTO toDeviceResponseDTO(Device device);
}
