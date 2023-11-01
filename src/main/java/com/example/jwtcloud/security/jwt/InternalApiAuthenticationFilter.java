//package com.example.jwtcloud.security.jwt;
//
//import com.example.jwtcloud.Jwtutils.SecurityUtils;
//import com.example.jwtcloud.security.UserPrincipal;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.logging.Logger;
//import org.slf4j.LoggerFactory;
//public class InternalApiAuthenticationFilter extends OncePerRequestFilter
//{
//
//    private static final Logger logger = (Logger) LoggerFactory.getLogger(InternalApiAuthenticationFilter.class);
//
//
//    private final String accessKey;
//
//    public InternalApiAuthenticationFilter(String accessKey)
//    {
//        this.accessKey = accessKey;
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
//    {
//        return !request.getRequestURI().startsWith("/api/internal");
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException
//    {
//        try
//        {
//            String requestKey = SecurityUtils.extrairetAuthenticationTokenFromrequest(request);
//
//            if (requestKey == null || !requestKey.equals(accessKey))
//            {
//                logger.warning("Internal key endpoint requested without/wrong key uri: {}");
//                throw new RuntimeException("UNAUTHORIZED");
//            }
///**creation de notre super user
// */
//            UserPrincipal user = UserPrincipal.createSuperUser();
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        catch (Exception ex)
//        {
//            logger.warning("Could not set user authentication in security context");
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
