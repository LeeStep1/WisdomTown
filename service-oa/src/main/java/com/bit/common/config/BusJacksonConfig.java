package com.bit.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;
import org.springframework.cloud.bus.event.UnknownRemoteApplicationEvent;
import org.springframework.cloud.bus.jackson.BusJacksonAutoConfiguration;
import org.springframework.cloud.bus.jackson.SubtypeModule;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.util.MimeTypeUtils;

import java.util.*;

/**
 * 覆盖BusJacksonAutoConfiguration的行为
 *
 * @author jianming.fan
 * @date 2019-01-16
 */
@Configuration
public class BusJacksonConfig extends BusJacksonAutoConfiguration {

    @Override
    public AbstractMessageConverter busJsonConverter(ObjectMapper objectMapper) {
        return new BusJacksonMessageConverter(objectMapper);
    }

    static class BusJacksonMessageConverter extends AbstractMessageConverter implements InitializingBean {
        private static final Log log = LogFactory.getLog(BusJacksonMessageConverter.class);
        private static final String DEFAULT_PACKAGE = ClassUtils.getPackageName(RemoteApplicationEvent.class);
        private final ObjectMapper mapper;
        private final boolean mapperCreated;
        private String[] packagesToScan;

        public BusJacksonMessageConverter() {
            this((ObjectMapper)null);
        }

        @Autowired(
                required = false
        )
        public BusJacksonMessageConverter(ObjectMapper objectMapper) {
            super(MimeTypeUtils.APPLICATION_JSON);
            this.packagesToScan = new String[]{DEFAULT_PACKAGE};
            if (objectMapper != null) {
                this.mapper = objectMapper;
                this.mapperCreated = false;
            } else {
                this.mapper = new ObjectMapper();
                this.mapperCreated = true;
            }

        }

        public boolean isMapperCreated() {
            return this.mapperCreated;
        }

        public void setPackagesToScan(String[] packagesToScan) {
            List<String> packages = new ArrayList(Arrays.asList(packagesToScan));
            if (!packages.contains(DEFAULT_PACKAGE)) {
                packages.add(DEFAULT_PACKAGE);
            }

            this.packagesToScan = (String[])packages.toArray(new String[0]);
        }

        private Class<?>[] findSubTypes() {
            List<Class<?>> types = new ArrayList();
            if (this.packagesToScan != null) {
                String[] var2 = this.packagesToScan;
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    String pkg = var2[var4];
                    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
                    provider.addIncludeFilter(new AssignableTypeFilter(RemoteApplicationEvent.class));
                    Set<BeanDefinition> components = provider.findCandidateComponents(pkg);
                    Iterator var8 = components.iterator();

                    while(var8.hasNext()) {
                        BeanDefinition component = (BeanDefinition)var8.next();

                        try {
                            types.add(Class.forName(component.getBeanClassName()));
                        } catch (ClassNotFoundException var11) {
                            throw new IllegalStateException("Failed to scan classpath for remote event classes", var11);
                        }
                    }
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("Found sub types: " + types);
            }

            return (Class[])types.toArray(new Class[0]);
        }

        @Override
        protected boolean supports(Class<?> aClass) {
            return RemoteApplicationEvent.class.isAssignableFrom(aClass);
        }

        @Override
        public Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
            Object result = null;

            try {
                Object payload = message.getPayload();
                if (payload instanceof byte[]) {
                    try {
                        result = this.mapper.readValue((byte[])((byte[])payload), targetClass);
                    } catch (InvalidTypeIdException var8) {
                        return new UnknownRemoteApplicationEvent(new Object(), var8.getTypeId(), (byte[])((byte[])payload));
                    }
                } else if (payload instanceof String) {
                    try {
                        result = this.mapper.readValue((String)payload, targetClass);
                    } catch (InvalidTypeIdException var7) {
                        return new UnknownRemoteApplicationEvent(new Object(), var7.getTypeId(), ((String)payload).getBytes());
                    }
                }

                return result;
            } catch (Exception var9) {
                this.logger.error(var9.getMessage(), var9);
                return null;
            }
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            this.mapper.registerModule(new SubtypeModule(this.findSubTypes()));
        }
    }
}
