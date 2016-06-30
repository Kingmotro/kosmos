/*
 * See provided LICENCE.txt in the project root.
 * Licenced to Minecraftly under GNU-GPLv3.
 */

package com.minecraftly.core;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Scanner;

/**
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class MinecraftlyUtil {

	/**
	 * Download text from a string url.
	 *
	 * @param url The url to download from.
	 * @return The text from the webpage.
	 * @throws IOException
	 */
	public static String downloadText( String url ) throws IOException {
		return downloadText( new URL( url ) );
	}

	/**
	 * Download text from a {@link URL}.
	 *
	 * @param url The url to download from.
	 * @return The text from the webpage.
	 * @throws IOException
	 */
	public static String downloadText( URL url ) throws IOException {
		return new Scanner( url.openStream() ).useDelimiter( "\\A" ).next();
	}

	/**
	 * Read text from a string filepath.
	 *
	 * @param filePath THe filepath of where to read.
	 * @return The string file contents.
	 * @throws IOException
	 */
	public static String readText( String filePath ) throws IOException {
		return readText( new File( filePath ) );
	}

	/**
	 * Read text from a {@link File}.
	 *
	 * @param file The file of where to read.
	 * @return The string file contents.
	 * @throws IOException
	 */
	public static String readText( File file ) throws IOException {
		return new Scanner( file ).useDelimiter( "\\A" ).next();
	}

	/**
	 * Convert milliseconds to minecraft ticks (20th of a second).
	 *
	 * @param milliseconds The milliseconds to convert from.
	 * @return The ticks converted from the millis.
	 */
	public static long convertMillisToTicks( long milliseconds ) {
		double nearestTickTime = round( milliseconds, 50 );
		return (long) ((nearestTickTime / 1000) * 20);
	}

	/**
	 * Round the value to the nearest factor.
	 *
	 * @param value  The value to round.
	 * @param factor The factor of where to round.
	 * @return The rounded value.
	 */
	public static double round( double value, double factor ) {
		return (Math.round( value / factor ) * factor);
	}

	/**
	 * Parse a string version of an address.
	 *
	 * @param address String representation of an IP:Port.
	 * @return The address.
	 */
	public static InetSocketAddress parseAddress( String address ) {

		String[] addressSections = address.split( ":" );
		int port = 25565;

		if ( addressSections.length < 1 )
			throw new IllegalArgumentException( "The address \"" + address + "\" is invalid." );

		if ( addressSections.length >= 2 ) {
			try {
				port = Integer.parseInt( addressSections[1].trim() );
			} catch ( NumberFormatException ex ) {
				port = 25565;
			}
		}

		return new InetSocketAddress( addressSections[0].trim(), port );

	}

}