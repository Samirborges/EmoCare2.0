package com.emocare.demo.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateBufferDTO(@NotNull @Min(0) @Max(30) Short bufferMinutes) { }
