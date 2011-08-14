/**
 * 
 */
package de.jbee.dying;

public interface IDecoder<T> {

	T decode( String value );
}