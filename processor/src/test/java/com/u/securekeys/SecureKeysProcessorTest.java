package com.u.securekeys;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import com.u.securekeys.mocks.Mocks;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.junit.Assert;
import org.junit.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class SecureKeysProcessorTest {

    @Test
    public void test_EmptyClassProcessedOk() {
        Compilation compilation = javac()
            .withProcessors(new SecureKeysProcessor())
            .compile(JavaFileObjects.forSourceString("EmptyClass", Mocks.MOCK_EMPTY));

        validate(compilation);
        Assert.assertTrue(stripClasses(compilation.generatedFiles()).isEmpty());
    }

    @Test
    public void test_SingleSecureKeyProcessedOk() {
        Compilation compilation = javac()
            .withProcessors(new SecureKeysProcessor())
            .compile(JavaFileObjects.forSourceString("SingleKeyClass", Mocks.MOCK_SECURE_KEY));

        validate(compilation);
        Assert.assertEquals(1, stripClasses(compilation.generatedFiles()).size());

        assertThat(compilation)
            .generatedSourceFile("com/u/securekeys/ProcessedMap")
            .hasSourceEquivalentTo(JavaFileObjects.forSourceString("ProcessedMap", Mocks.MOCK_SECURE_KEY_GEN_FILE));
    }

    @Test
    public void test_MultipleSecureKeyProcessedOk() {
        Compilation compilation = javac()
            .withProcessors(new SecureKeysProcessor())
            .compile(JavaFileObjects.forSourceString("MultipleKeyClass", Mocks.MOCK_SECURE_KEY_MULTIPLE));

        validate(compilation);
        Assert.assertEquals(1, stripClasses(compilation.generatedFiles()).size());

        assertThat(compilation)
            .generatedSourceFile("com/u/securekeys/ProcessedMap")
            .hasSourceEquivalentTo(JavaFileObjects.forSourceString("ProcessedMap", Mocks.MOCK_SECURE_KEY_MULTIPLE_GEN_FILE));
    }

    void validate(Compilation compilation) {
        // Check that it succeded
        assertThat(compilation).succeeded();

        // Check that only the warning of the classpath conjunction of source is shown, no more.
        for (Diagnostic diagnostic : compilation.warnings()) {
            if (!diagnostic.getMessage(Locale.ENGLISH).contains("Supported source version 'RELEASE_7' from annotation processor")) {
                Assert.fail("Warnings found in the compilation: " + diagnostic.getMessage(Locale.ENGLISH));
            }
        }
    }

    List<JavaFileObject> stripClasses(List<JavaFileObject> files) {
        List<JavaFileObject> newList = new ArrayList<>();
        for (JavaFileObject fileObject : files) {
            if (fileObject.getKind() != JavaFileObject.Kind.CLASS) {
                newList.add(fileObject);
            }
        }
        return newList;
    }

}