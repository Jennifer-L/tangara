/**
 * Tangara is an educational platform to get started with programming.
 * Copyright (C) 2008-2012 Colombbus (http://www.colombbus.org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.colombbus.build;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * Generator of the index file which contains the mapping (language/package,
 * class name)
 */
public class LocalizeIndex implements Closeable {
	//FIXME Is the file generated by this class is used or not ?

	private Filer filer;
	private FileObject logFile;
	private Writer writer;

	/**
	 *
	 * @param env
	 *            processing environment
	 * @param baseClass
	 *            class to localize
	 * @throws IOException
	 */
	public LocalizeIndex(ProcessingEnvironment env, TypeElement baseClass) throws IOException {
		this.filer = env.getFiler();
		initLogFile(baseClass);
		initWriter();
	}

	private void initLogFile(TypeElement baseClass) throws IOException {
		String classname = TypeElementHelper.extractClassName(baseClass);
		String packageName = TypeElementHelper.extractPackageName(baseClass);
		String filename = classname + "_localization.txt"; //$NON-NLS-1$
		this.logFile = filer.createResource(StandardLocation.SOURCE_OUTPUT, packageName, filename);
	}

	private void initWriter() throws IOException {
		this.writer = logFile.openWriter();
	}

	/**
	 * Register a new localized class
	 *
	 * @param language
	 *            the localization language
	 * @param className
	 *            the localized class name
	 * @throws IOException
	 */
	public void register(String language, String className) throws IOException {
		String line = String.format("%s %s\n", language, className); //$NON-NLS-1$
		writer.append(line);
		writer.flush();
	}

	@Override
	public void close() throws IOException {
		if (writer != null)
			writer.close();
	}

}