package CHILD.Enum;

public enum TTS_ERROR
{
	PLAYING("OK"), INDEX_ENDED("INDEX_VALUE_OVER_ERROR"), MEMVER_VAR_NULL_ERROR("MEMVER_VARIOUS_INVALID_ERROR"), STRING_NULL_OR_EMTPY(
			"STRING_ERROR"), HTTP_ERROR("HTTP_ERROR"), REGEX_ERROR("REGEX_ERROR"), IO_STREAM_ERROR("FILE_SAVE_ERROR");

	private String _value;

	// 열거 값에 (String) 값 span 에 대입
	TTS_ERROR(String months)
	{
		_value = months;
	}

	// span 값 반환
	public String getString()
	{
		return _value;
	}
}