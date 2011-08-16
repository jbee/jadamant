package de.jbee.util;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePeriod;
import org.joda.time.Seconds;


public final class JodaDecoder {

	private JodaDecoder() {
		// util
	}

	static {
		Decoder.register( new SecondsDecoder(), Seconds.class );
		Decoder.register( new HoursDecoder(), Hours.class );
		Decoder.register( new IntervalDecoder(), Interval.class, ReadableInterval.class );
		Decoder.register( new DurationDecoder(), Duration.class, ReadableDuration.class );
		Decoder.register( new PeriodDecoder(), Period.class, ReadablePeriod.class );
		Decoder.register( new DateMidnightDecoder(), DateMidnight.class );
		Decoder.register( new DateTimeDecoder(), DateTime.class, ReadableDateTime.class,
				ReadableInstant.class );
	}

	/**
	 * Has to be executed to load this class so it will {@link Decoder#register(IDecoder, Class)}
	 * its decoders properly.
	 */
	public static void makeAvailable() {
		// happends on load of class, which is triggered by calling this method even if it doesn't
		// do anything - this will ensure, that decoders only registered once
	}

	static final class SecondsDecoder
			implements IDecoder<Seconds> {

		@Override
		public Seconds decode( String value ) {
			return Seconds.seconds( Integer.valueOf( value.replaceAll( "\\s*(sec|s)\\s*", "" ) ) );
		}
	}

	static final class HoursDecoder
			implements IDecoder<Hours> {

		@Override
		public Hours decode( String value ) {
			return Hours.hours( Integer.valueOf( value.replaceAll( "\\s*h\\s*", "" ) ) );
		}
	}

	static final class PeriodDecoder
			implements IDecoder<Period> {

		@Override
		public Period decode( String value ) {
			return new Period( value );
		}
	}

	static final class DurationDecoder
			implements IDecoder<Duration> {

		@Override
		public Duration decode( String value ) {
			return new Duration( value );
		}
	}

	static final class IntervalDecoder
			implements IDecoder<Interval> {

		@Override
		public Interval decode( String value ) {
			return new Interval( value );
		}
	}

	static final class DateTimeDecoder
			implements IDecoder<DateTime> {

		@Override
		public DateTime decode( String value ) {
			return new DateTime( value );
		}
	}

	static final class DateMidnightDecoder
			implements IDecoder<DateMidnight> {

		@Override
		public DateMidnight decode( String value ) {
			return new DateMidnight( value );
		}
	}
}
