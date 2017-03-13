package com.u.securekeys;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.u.securekeys.annotation.SecureKey;
import com.u.securekeys.annotation.SecureKeys;
import com.u.securekeys.internal.Encoder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes({ SecureKey.CLASSPATH, SecureKeys.CLASSPATH })
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class SecureKeysProcessor extends AbstractProcessor {

    /**
     * Remember that the SecureKeys.java inside core references this class!
     */
    private static final String CLASS_NAME = "ProcessedMap";
    private static final String CLASS_CLASSPATH = "com.u.securekeys";

    @Override
    public boolean process(final Set<? extends TypeElement> set, final RoundEnvironment roundEnvironment) {
        List<SecureKey> annotations = flattenElements(
            roundEnvironment.getElementsAnnotatedWith(SecureKey.class),
            roundEnvironment.getElementsAnnotatedWith(SecureKeys.class)
        );

        MethodSpec.Builder retrieveMethodBuilder = MethodSpec.methodBuilder("retrieve")
            .addModifiers(Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
            .returns(String[].class)
            .addStatement("String array[] = new String[" + annotations.size() + "]");

        int counter = 0;
        Encoder encoder = new Encoder();
        for (SecureKey annotation : annotations) {
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
        } catch (IOException e) { /* Silent. */ }

        return true;
    }

    private List<SecureKey> flattenElements(Set<? extends Element> secureKeyElements,
            Set<? extends Element> secureKeysElements) {
        List<SecureKey> result = new ArrayList<>();

        for (Element element : secureKeyElements) {
            result.add(element.getAnnotation(SecureKey.class));
        }

        for (Element element : secureKeysElements) {
            result.addAll(Arrays.asList(element.getAnnotation(SecureKeys.class).value()));
        }

        return result;
    }

}