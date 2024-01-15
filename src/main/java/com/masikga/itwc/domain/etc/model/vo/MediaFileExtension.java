package com.masikga.itwc.domain.etc.model.vo;

import java.util.Arrays;

public enum MediaFileExtension {
	JPEG, JPG, PNG, GIF, MP4, YOU_TUBE_URL;

	public static Boolean isSupportedType(String stringValue) {
		return Arrays.stream(MediaFileExtension.values())
			.anyMatch(extension -> extension.name().equals(stringValue));
	}
}
