/**
 * 
 */
package de.jbee.util;

public interface IDecoder<T> {

	T decode( String value );
}