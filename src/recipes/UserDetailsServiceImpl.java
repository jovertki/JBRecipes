package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepo;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepo.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
    }
}