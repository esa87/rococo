package rococo.service;

import rococo.data.repository.UserRepository;
import rococo.domain.RococoUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RococoUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public RococoUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(RococoUserPrincipal::new)
        .orElseThrow(() -> new UsernameNotFoundException("Username: `" + username + "` not found"));
  }
}
