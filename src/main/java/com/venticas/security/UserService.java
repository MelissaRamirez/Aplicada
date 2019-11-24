/*
-
********************************************************************************
*
* 
* 
* 
* 
	* * Trabajado por Luis
 */
package com.venticas.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.venticas.business.UserBusiness;
import com.venticas.domain.User;

// 
/**
 * The Class UserService.
 */
@Service
@Transactional
public class UserService implements UserDetailsService {

	/** The user data. */
	@Autowired
	private UserBusiness userBusiness;

		/**
		 * Este método se utiliza para traer el detalle del usuario.
		 *
		 * @param userName the user name
		 * @return the user details
		 * @throws UsernameNotFoundException the username not found exception
		 */
		@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userBusiness.findUserByUserName(userName);
		if (user.getId() == 0)
			throw new UsernameNotFoundException("UserName " + userName + " not found");
		else
			return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthorities(user));
		
	}
	
	
	/**
	 * Este método busca las autoridades de un usuario.
	 *
	 * @param user el usuario que se le desean buscar las autoridades.
	 * @return the authorities, lista de las autoridades del usuario.
	 */
	private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
			String role = user.getRole().getTypeName();
			Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);
			return authorities;
	}



}
