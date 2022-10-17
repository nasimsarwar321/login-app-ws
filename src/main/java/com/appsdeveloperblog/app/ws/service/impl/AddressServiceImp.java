package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.AddressRepository;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;

@Service
public class AddressServiceImp implements AddressService {

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public List<AddressDTO> getAddresses(String userId) {

		List<AddressDTO> returnValue = new ArrayList<>();
		ModelMapper mapper = new ModelMapper();
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			return returnValue;

		Iterable<AddressEntity> listAddressEntities = addressRepository.findAllByUserDetails(userEntity);
		
		for(AddressEntity addressEntity : listAddressEntities) {
			returnValue.add(mapper.map(addressEntity, AddressDTO.class));
		}

		return returnValue;
	}

	@Override
	public AddressDTO getAddress(String addressId) {
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		ModelMapper mapper = new ModelMapper();
		AddressDTO returnValue = mapper.map(addressEntity, AddressDTO.class);
		return returnValue;
	}

}
