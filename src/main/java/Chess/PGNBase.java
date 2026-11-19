package Chess;

public abstract class PGNBase {

	protected static final String RESULT_DRAW = "1/2-1/2";
	protected static final String RESULT_WHITE_WIN = "1-0";
	protected static final String RESULT_BLACK_WIN = "0-1";
	protected static final String RESULT_UNFINISHED = "*";

	protected static final String SHORT_CASTLING = "O-O";
	protected static final String LONG_CASTLING = "O-O-O";

	protected static final String CHECK_SYMBOL = "+";
	protected static final String CHECKMATE_SYMBOL = "#";
	protected static final String CAPTURE_SYMBOL = "x";
	protected static final String PROMOTION_SYMBOL = "=";
	protected static final String MOVE_NUMBER_SYMBOL = ".";

	protected static final String TAG_OPENING_SYMBOL = "[";
	protected static final String TAG_CLOSING_SYMBOL = "]";
	protected static final String TAG_VALUE_SYMBOL = "\"";
}
