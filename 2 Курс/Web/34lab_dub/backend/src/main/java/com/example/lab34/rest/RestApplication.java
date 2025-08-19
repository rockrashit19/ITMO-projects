import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class RestApplication extends Application {
    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<>();
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("http://localhost:3000");
        corsFilter.setAllowedMethods("GET, POST, PUT, DELETE, OPTIONS");
        corsFilter.setAllowedHeaders("Content-Type, Authorization");
        corsFilter.setAllowCredentials(true);
        singletons.add(corsFilter);
        return singletons;
    }
}