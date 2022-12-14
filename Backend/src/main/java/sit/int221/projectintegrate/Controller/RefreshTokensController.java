package sit.int221.projectintegrate.Controller;

import io.jsonwebtoken.impl.DefaultClaims;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.int221.projectintegrate.DTO.AuthenticationResponse;
import sit.int221.projectintegrate.Entities.User;
import sit.int221.projectintegrate.Util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/api"})
@CrossOrigin
public class RefreshTokensController {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping( "/refresh")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
        // From the HttpRequest get the claims
        User addUserList = modelMapper.map(request, User.class);
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String token = jwtUtil.createRefreshToken(expectedMap, expectedMap.get("sub").toString());
        String roles = addUserList.getRoles();
        String email = addUserList.getEmail();
        System.out.println(addUserList.getRoles());
        return ResponseEntity.ok(new AuthenticationResponse(token,roles,email));
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }
}
