package com.teb.practice.controller;

public record ApiResponse<T>(T data, String message) {}
