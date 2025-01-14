package com.demo.voucher.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VoucherTypeTest {


    private static Stream<Arguments> voucherTypeDescription() {
        return Stream.of(
                Arguments.of(VoucherType.FIXED_AMOUNT, "고정 할인 바우처"),
                Arguments.of(VoucherType.PERCENT_DISCOUNT, "비율 할인 바우처")
        );
    }

    private static Stream<Arguments> voucherTypeAmountDescription() {
        return Stream.of(
                Arguments.of(VoucherType.FIXED_AMOUNT, "할인 고정 금액은 1 이상의 정수여야 합니다. 할인 금액을 입력해주세요 : "),
                Arguments.of(VoucherType.PERCENT_DISCOUNT, "할인 비율은 1 이상 99 이하의 정수여야 합니다. 할인 비율을 입력해주세요 : ")
        );
    }

    private static Stream<Arguments> voucherTypeToString() {
        return Stream.of(
                Arguments.of(VoucherType.FIXED_AMOUNT, "1: 고정 할인 바우처"),
                Arguments.of(VoucherType.PERCENT_DISCOUNT, "2: 비율 할인 바우처")
        );
    }

    @ParameterizedTest
    @CsvSource({"1, 고정 할인 바우처", "2, 비율 할인 바우처"})
    @DisplayName("입력 받는 Voucher Type Command(1,2)를 통해 어떤 Voucher Type인지 검증해내는 테스트")
    void getVoucherTypeByCommand(String inputType, String voucherDescription) {
        assertEquals(VoucherType.of(inputType).getVoucherDescription(), voucherDescription);

    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "300", "1000", "5000"})
    @DisplayName("FIXED_AMOUNT 바우처 타입의 할인 value가 1 이상의 정수일 때 등록한 Pattern과 맞는지 검증하는 테스트")
    void validateAmount_fixed_type(String amount) {
        assertTrue(VoucherType.FIXED_AMOUNT.isValidAmount(amount));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "240.5", "one thousand"})
    @DisplayName("FIXED_AMOUNT 바우처 타입의 할인 value가 1 이상의 정수가 아닌 경우 등록한 Pattern과 맞지 않는지 검증하는 테스트")
    void validateAmount_fixed_type_fail(String amount) {
        // given
        String expectedMessage = "올바른 할인 금액을 입력하지 않았습니다.";

        // when
        Exception exception = assertThrows(RuntimeException.class, () -> VoucherType.FIXED_AMOUNT.isValidAmount(amount));

        // then
        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedMessage);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "20", "99", "50"})
    @DisplayName("PERCENT_AMOUNT 바우처 타입의 할인 value가 1 이상, 99 이하의 정수일 때 등록한 Pattern과 맞는지 검증하는 테스트")
    void validateAmount_percent_type(String amount) {
        assertTrue(VoucherType.PERCENT_DISCOUNT.isValidAmount(amount));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-20", "99.5", "55.5", "100", "fifty percent"})
    @DisplayName("PERCENT_AMOUNT 바우처 타입의 할인 value가 1 이상, 99 이하의 정수가 아닐 때 등록한 Pattern과 맞지 않는지 검증하는 테스트")
    void validateAmount_percent_type_fail(String amount) {
        // given
        String expectedMessage = "올바른 할인 금액을 입력하지 않았습니다.";

        // when
        Exception exception = assertThrows(RuntimeException.class, () -> VoucherType.PERCENT_DISCOUNT.isValidAmount(amount));

        // then
        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedMessage);
    }

    @ParameterizedTest
    @MethodSource("voucherTypeDescription")
    @DisplayName("voucher type에 따라 voucher에 대한 설명을 제대로 가져오는지 확인하는 테스트")
    void getAmountDescription(VoucherType voucherType, String voucherDescription) {
        assertEquals(voucherType.getVoucherDescription(), voucherDescription);
    }

    @ParameterizedTest
    @MethodSource("voucherTypeAmountDescription")
    @DisplayName("Voucher Type에 따라 사용자로부터 요청하는 할인 amount 입력 값에 대한 설명을 잘 가져오는지 확인하는 테스트")
    void getVoucherDescription(VoucherType voucherType, String amountDescription) {
        assertEquals(voucherType.getRequestAmountDescription(), amountDescription);
    }

    @ParameterizedTest
    @MethodSource("voucherTypeToString")
    @DisplayName("Voucher Type을 DTO로 변환하는지 검증하는 테스트")
    void testToString(VoucherType voucherType, String expectedToString) {
        assertEquals(voucherType.getVoucherCommandAndDescription().toString(), expectedToString);
    }

    @Test
    @DisplayName("VoucherType의 value들 확인하는 테스트")
    void values() {
        assertThat(Arrays.stream(VoucherType.values()))
                .hasSize(2)
                .contains(VoucherType.FIXED_AMOUNT)
                .contains(VoucherType.PERCENT_DISCOUNT);
    }
}