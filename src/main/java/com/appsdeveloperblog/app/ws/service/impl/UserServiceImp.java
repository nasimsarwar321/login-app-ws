package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.appsdeveloperblog.app.ws.shared.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.exception.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;

//Author Nasim_Sarwar
@Service
public class UserServiceImp implements UserService {
	@Autowired
	UserRepository userRespository;
	@Autowired
	Utils utils;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {
		ModelMapper mapper = new ModelMapper();
		if (userRespository.findByEmail(user.getEmail()) != null)
			throw new RuntimeException("Record already exists");

		UserEntity userEntity = mapper.map(user, UserEntity.class);
		userEntity.setUserId(utils.generateUserId(20));
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		UserEntity userStoredDetails = userRespository.save(userEntity);
		UserDto returnValue = mapper.map(userStoredDetails, UserDto.class);
		return returnValue;

	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRespository.findByEmail(email);
		ModelMapper mapper = new ModelMapper();
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		return returnValue;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRespository.findByUserId(userId);
		ModelMapper mapper = new ModelMapper();
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		return returnValue;

	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		UserEntity userEntity = userRespository.findByUserId(userId);
		ModelMapper mapper = new ModelMapper();
		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		UserEntity storedUserEntity = userRespository.save(userEntity);
		UserDto updatedUserDetail = mapper.map(storedUserEntity, UserDto.class);
		return updatedUserDetail;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRespository.findByUserId(userId);
		userRespository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();

		if (page > 0)
			page = page - 1;

		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<UserEntity> usersPage = userRespository.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();

		for (UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}

		return returnValue;
	}

	@Override
	public boolean verifyEmailToken(String token) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requestPasswordReset(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resetPassword(String token, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRespository.findByEmail(username);
		if (userEntity == null)
			throw new UsernameNotFoundException("use is not exists");
		return new User(username, userEntity.getEncryptedPassword(), new ArrayList<>());
	}

}
