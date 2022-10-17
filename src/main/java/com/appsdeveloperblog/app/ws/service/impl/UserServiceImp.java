package com.appsdeveloperblog.app.ws.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appsdeveloperblog.app.ws.io.entity.PasswordResetTokenEntity;
import com.appsdeveloperblog.app.ws.io.repository.PasswordResetTokenRepository;
import com.appsdeveloperblog.app.ws.service.EmailService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.ui.model.request.MailRequest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
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
     @Autowired
     PasswordResetTokenRepository passwordResetTokenRepository;


    @Autowired
    private Configuration config;

    @Autowired
    private EmailService service;
    @Override
    public UserDto createUser(UserDto user) {
        ModelMapper mapper = new ModelMapper();
        if (userRespository.findByEmail(user.getEmail()) != null)
            throw new RuntimeException("Record already exists");
        user.getAddresses().forEach(e -> e.setAddressId(utils.generateAddressId(20)));
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        //userEntity.setAddresses(listAddressEntities);
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
        boolean returnValue = false;

        // Find user by token
        UserEntity userEntity = userRespository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean hastokenExpired = Utils.hasTokenExpired(token);
            if (!hastokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRespository.save(userEntity);
                returnValue = true;
            }
        }
        return returnValue;

    }

    @Override
    public boolean requestPasswordReset(String email) {


        boolean returnValue = false;

        UserEntity userEntity = userRespository.findByEmail(email);
        Map<String, Object> model = new HashMap<>();

        if (userEntity == null) {
            return returnValue;
        }

        String token = new Utils().generatePasswordResetToken(userEntity.getUserId());

        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);

        MailRequest request = new MailRequest();
        request.setFirstName(userEntity.getFirstName());
        request.setLastName(userEntity.getLastName());
        request.setTo(userEntity.getEmail());
        request.setFrom("atoztraders39@gmail.com");
        request.setSubject("PASSWORD RESET ");


        model.put("firstName",userEntity.getFirstName() );
        model.put("lastName",userEntity.getFirstName() );
        model.put("location", "Bangalore,India");
        model.put("TOKEN", token);
        try{

            Template t = config.getTemplate("password-reset-template.ftl");

            service.sendEmail(request, model,t );
            Template tem = config.getTemplate("email-template.ftl");
            request.setSubject("Login_System");
            service.sendEmail(request, model,tem );
        }
        catch (IOException t ){
          throw  new RuntimeException("Mail Sending failure : "+t.getMessage());
           // response.setStatus(Boolean.FALSE);
        }



       /**
        * Sending mail to User for Passoword reset
        * returnValue = new AmazonSES().sendPasswordResetRequest(
                userEntity.getFirstName(),
                userEntity.getEmail(),
                token);*/

        return returnValue;


    }

    @Override
    public boolean resetPassword(String token, String password) {
        boolean returnValue = false;

        if (Utils.hasTokenExpired(token)) {
            return returnValue;
        }

        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return returnValue;
        }

        // Prepare new password
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        // Update User password in database
        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity savedUserEntity = userRespository.save(userEntity);

        // Verify if password was saved successfully
        if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }

        // Remove Password Reset token from database
        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        return returnValue;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRespository.findByEmail(username);
        if (userEntity == null)
            throw new UsernameNotFoundException("use is not exists");
        return new User(username, userEntity.getEncryptedPassword(), new ArrayList<>());
    }

}
