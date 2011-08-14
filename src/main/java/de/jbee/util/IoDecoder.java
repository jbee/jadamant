package de.jbee.util;

import de.jbee.dying.IDecoder;

public final class IoDecoder {

	private IoDecoder() {
		// util
	}

	static {
		Decoder.register( new FileDecoder(), Filename.class );
		Decoder.register( new PathDecoder(), Dirname.class );
	}

	/**
	 * Has to be executed to load this class so it will {@link Decoder#register(IDecoder, Class)}
	 * its decoders properly.
	 */
	public static void makeAvailable() {
		// happends on load of class, which is triggered by calling this method even if it doesn't
		// do anything - this will ensure, that decoders only registered once
	}

	static final class FileDecoder
			implements IDecoder<Filename> {

		@Override
		public Filename decode( String value ) {
			return Filename.valueOf( value );
		}
	}

	static final class PathDecoder
			implements IDecoder<Dirname> {

		@Override
		public Dirname decode( String value ) {
			return Dirname.valueOf( value );
		}
	}
}
