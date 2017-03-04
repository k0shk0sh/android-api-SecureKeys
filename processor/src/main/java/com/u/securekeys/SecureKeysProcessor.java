package com.u.securekeys;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.u.securekeys.annotation.SecureKey;
import com.u.securekeys.internal.Encoder;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes(SecureKey.CLASSPATH)
public class SecureKeysProcessor extends AbstractProcessor {

    /**
     * Remember this key is also in the decoder inside
     * core::main/cpp/native-lib.cpp !
     *
     * If you plan to change it, also change it in the decoder, else
     * conflicts will arise.
     */
    private static final String ENCODER_KEY = "$]3Û@5ml@";

    /**
     * Remember that the SecureKeys.java inside core references this class!
     */
    private static final String CLASS_NAME = "ProcessedMap";
    private static final String CLASS_CLASSPATH = "com.u.securekeys";

    @Override
    public boolean process(final Set<? extends TypeElement> set, final RoundEnvironment roundEnvironment) {
        Collection<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(SecureKey.class);

        MethodSpec.Builder retrieveMethodBuilder = MethodSpec.methodBuilder("retrieve")
            .addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
            .returns(String[].class)
            .addStatement("String array[] = new String[" + annotatedElements.size() + "]");

        int counter = 0;
        Encoder encoder = new Encoder(ENCODER_KEY);
        for (Element element : annotatedElements) {
            SecureKey annotation = element.getAnnotation(SecureKey.class);

            String key = encoder.encode(annotation.key());
            String value = encoder.encode(annotation.value());

            retrieveMethodBuilder.addStatement("array[" + counter + "] = \"" + key + ";;;;" +
                value + "\"");

            ++counter;
        }

        retrieveMethodBuilder.addStatement("return array");

        TypeSpec createdClass = TypeSpec.classBuilder(CLASS_NAME)
            .addModifiers(Modifier.FINAL)
            .addMethod(retrieveMethodBuilder.build())
            .build();

        JavaFile javaFile = JavaFile.builder(CLASS_CLASSPATH, createdClass)
            .addFileComment("Method that retrieves the mapping of the values")
            .build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {}

        return true;
    }

}