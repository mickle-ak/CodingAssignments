package org.mickleak.taskmanagementsystem.server.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class JacksonConfiguration {


	@Bean
	public Module customModule() {
		SimpleModule customModule = new SimpleModule();
		customModule.addDeserializer( String.class, new StrictStringDeserializer() );
		return customModule;
	}


	private static class StrictStringDeserializer extends StringDeserializer implements ContextualDeserializer {


		public static final String MODEL_PACKAGE_NAME = "org.mickleak.taskmanagementsystem.server.api";

		@Override
		public String deserialize( JsonParser p, DeserializationContext ctxt ) throws IOException {
			JsonToken token = p.currentToken();
			if( token.isBoolean()
			    || token.isNumeric()
			    || !token.toString().equalsIgnoreCase( "VALUE_STRING" ) ) {
				ctxt.reportInputMismatch( String.class, "%s is not a `String` value!", token.toString() );
				return null;
			}
			return super.deserialize( p, ctxt );

		}

		@Override
		public JsonDeserializer<?> createContextual( final DeserializationContext ctxt, final BeanProperty property ) {
			if (property != null) {
				Class<?> containingClass = property.getMember().getDeclaringClass();
				if (containingClass.getPackageName().startsWith(MODEL_PACKAGE_NAME)) {
					return this;  // use this deserializer for classes in the package
				}
			}
			return new StringDeserializer();  // Use default deserializer for other classes
		}
	}
}
