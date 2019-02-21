/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

/**
 * Utility class for creating Java POJOs from documentation source.
 *
 * @author  Middleware Services
 */
public class ClassGenerator
{

  /** Package to create classes in. */
  private static final String PACKAGE_TO_CREATE = "org.passay.doc";

  /** Packages to import for compilation. */
  private static final String[] PACKAGES_TO_IMPORT = new String[] {
    "java.io",
    "java.util",
    "org.cryptacular.bean",
    "org.cryptacular.spec",
    "org.passay",
    "org.passay.dictionary",
    "org.passay.dictionary.sort",
    "org.passay.entropy",
  };

  /** String containing all import statements. */
  private static final String IMPORT_STATEMENTS;

  /** Buffer size for reading and writing files. */
  private static final int BUFFER_SIZE = 2048;

  /** Code model for java class creation. */
  private final JCodeModel codeModel = new JCodeModel();

  /** Sections to build beans for. */
  private final Map<String, List<String>> sections = new HashMap<>();

  /** Initialize {@link #IMPORT_STATEMENTS}. */
  static {
    final StringBuilder sb = new StringBuilder();
    for (String p : PACKAGES_TO_IMPORT) {
      sb.append("import ").append(p).append(".*;").append("\n");
    }
    IMPORT_STATEMENTS = sb.toString();
  }


  /**
   * Creates a new class generator.
   *
   * @param  url  to download zipped source from
   * @param  path  to write files to
   *
   * @throws  IOException  if the source cannot be downloaded, unzipped and read
   */
  public ClassGenerator(final String url, final String path)
    throws IOException
  {
    // download the zipped source
    download(url, path + "/source.zip");

    // unzip the source
    unzip(path + "/source.zip", path + "/doc-sources");

    // read the source files
    final Path sourceDir = Paths.get(path + "/doc-sources");
    Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
        throws IOException
      {
        if (attrs.isRegularFile()) {
          final String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
          final String name = file.getName(file.getNameCount() - 2).toString();
          final List<String> l;
          if (sections.containsKey(name)) {
            l = sections.get(name);
          } else {
            l = new ArrayList<>();
          }
          l.add(content);
          sections.put(name, l);
        }
        return FileVisitResult.CONTINUE;
      }
    });
  }


  /**
   * Download a file.
   *
   * @param  url  to download
   * @param  destination  to write the file at
   *
   * @throws  IOException  if an error occurs
   */
  private static void download(final String url, final String destination)
    throws IOException
  {
    final URL download = new URL(url);
    final ReadableByteChannel rbc = Channels.newChannel(download.openStream());
    final FileOutputStream fos = new FileOutputStream(destination);
    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    fos.flush();
    rbc.close();
  }


  /**
   * Unzip a file.
   *
   * @param  file  to unzip
   * @param  destination  to expand the file at
   *
   * @throws  IOException  if an error occurs
   */
  private static void unzip(final String file, final String destination)
    throws IOException
  {
    final File directory = new File(destination);
    // if the output directory doesn't exist, create it
    if (!directory.exists()) {
      directory.mkdirs();
    }
    // buffer for read and write data to file
    final byte[] buffer = new byte[BUFFER_SIZE];
    final ZipInputStream zipInput = new ZipInputStream(new FileInputStream(file));
    ZipEntry entry = zipInput.getNextEntry();
    try {
      while (entry != null) {
        final String entryName = entry.getName();
        final File f = new File(destination + File.separator + entryName);
        // create the directories of the zip directory
        if (entry.isDirectory()) {
          final File newDir = new File(f.getAbsolutePath());
          if (!newDir.exists()) {
            newDir.mkdirs();
          }
        } else {
          try (FileOutputStream fOutput = new FileOutputStream(f)) {
            int count;
            while ((count = zipInput.read(buffer)) > 0) {
              // write 'count' bytes to the file output stream
              fOutput.write(buffer, 0, count);
            }
          }
        }
        // close ZipEntry and take the next one
        zipInput.closeEntry();
        entry = zipInput.getNextEntry();
      }
    } finally {
      // close the last ZipEntry
      zipInput.closeEntry();
      zipInput.close();
    }
  }


  /**
   * Generates a class for each doc section.
   */
  public void generate()
  {
    for (Map.Entry<String, List<String>> entry : sections.entrySet()) {
      final JDefinedClass definedClass = createClass(PACKAGE_TO_CREATE, entry.getKey());
      final JDocComment jDocComment = definedClass.javadoc();
      jDocComment.add(String.format("Passay generated bean for section '%s'", entry.getKey()));

      final List<String> names = entry.getValue();
      for (int i = 0; i < names.size(); i++) {
        createMethod(definedClass, entry.getKey() + (i + 1), names.get(i));
      }
    }
  }


  /**
   * Creates a class in the supplied package.
   *
   * @param  classPackage  to place the class in
   * @param  className  to create
   *
   * @return  class
   *
   * @throws  IllegalArgumentException  if the class already exists
   */
  protected JDefinedClass createClass(final String classPackage, final String className)
  {
    final String fqClassName;
    if (!Character.isUpperCase(className.charAt(0))) {
      fqClassName = String.format(
        "%s.%s",
        classPackage,
        className.substring(0, 1).toUpperCase() + className.substring(1, className.length()));
    } else {
      fqClassName = String.format("%s.%s", classPackage, className);
    }

    try {
      return codeModel._class(fqClassName);
    } catch (JClassAlreadyExistsException e) {
      throw new IllegalArgumentException("Class already exists: " + fqClassName, e);
    }
  }


  /**
   * Creates a method on the supplied class with the supplied name.
   *
   * @param  clazz  to put getter and setter methods on
   * @param  name  of the property
   * @param  body  content of the method
   */
  protected void createMethod(final JDefinedClass clazz,  final String name, final String body)
  {
    final JMethod method = clazz.method(JMod.PUBLIC, Void.TYPE, name);
    method._throws(Exception.class);
    method.body().directStatement(body);
  }


  /**
   * Writes the generated classes to disk at the supplied path.
   *
   * @param  path  to write the classes to
   *
   * @throws  IOException  if the write fails
   */
  public void write(final String path)
    throws IOException
  {
    final File f = new File(path);
    if (!f.exists()) {
      f.mkdirs();
    }
    codeModel.build(f);

    // add imports
    final Path sourceDir = Paths.get(path);
    Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
        throws IOException
      {
        if (attrs.isRegularFile()) {
          String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
          content = content.replaceFirst(
            "package org.passay.doc;",
            "package org.passay.doc;\n\n" + IMPORT_STATEMENTS);
          Files.write(file, content.getBytes(StandardCharsets.UTF_8));
        }
        return FileVisitResult.CONTINUE;
      }
    });

  }


  /**
   * Provides command line access to a {@link ClassGenerator}. Expects two arguments:
   *
   * <ol>
   *   <li>url to source zip file</li>
   *   <li>target directory to write files to</li>
   * </ol>
   *
   * @param  args  command line arguments
   *
   * @throws  Exception  if any error occurs
   */
  public static void main(final String[] args)
    throws Exception
  {
    final String url = args[0];
    final String targetPath = args[1];

    final ClassGenerator generator = new ClassGenerator(url, targetPath);
    generator.generate();
    generator.write(targetPath + "/generated-test-sources/passay-docs");
  }
}
