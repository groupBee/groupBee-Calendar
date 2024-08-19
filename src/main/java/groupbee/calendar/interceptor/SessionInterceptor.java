package groupbee.calendar.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String springSession = request.getHeader("Cookie");
            //String springSession = "SESSION=OTM4NGY3YjAtMDMxNi00MTdmLTgzNDctOWEyY2EyZWJmNjhh";
            System.out.println(springSession);
            template.header("Cookie", springSession);
        }
    }
}
