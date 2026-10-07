package Di.Pierro.application.validators.cnpj;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class CnpjValidator implements ConstraintValidator<CNPJ, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return validatorCnpj(value);
    }

    public boolean validatorCnpj(String cnpj) {
        if (cnpj == null) {
            return false;
        }

        String cleanedCnpj = cnpj.replaceAll("[^0-9*]", "");

        boolean cnpjHasCorrectLength = cleanedCnpj.length() == 14;
        boolean cnpjIsIncompleteButRight =
                cnpjHasCorrectLength && cleanedCnpj.contains("*");

        if (cnpjIsIncompleteButRight) {
            return true;
        }

        String numericCnpj = cleanedCnpj.replaceAll("\\D", "");

        boolean cnpjIsComplete =
                numericCnpj.length() == 14
                        && isNumeric(numericCnpj)
                        && !isRepeatedNumbers(numericCnpj);

        if (cnpjIsComplete) {
            List<Integer> cnpjIntegersList = getTwelveCnpjDigits(numericCnpj);
            List<Integer> cnpjToCompare = getCnpjDigits(numericCnpj);

            int firstVerificationDigit =
                    getVerificationDigit(cnpjIntegersList, true);

            cnpjIntegersList.add(firstVerificationDigit);

            int secondVerificationDigit =
                    getVerificationDigit(cnpjIntegersList, false);

            cnpjIntegersList.add(secondVerificationDigit);

            return areEqual(cnpjIntegersList, cnpjToCompare);
        }

        return false;
    }

    private boolean areEqual(
            List<Integer> cnpjIntegersList,
            List<Integer> cnpjToCompare
    ) {
        if (cnpjIntegersList.size() != cnpjToCompare.size()) {
            return false;
        }

        for (int i = 0; i < cnpjToCompare.size(); i++) {
            if (!cnpjToCompare.get(i).equals(cnpjIntegersList.get(i))) {
                return false;
            }
        }

        return true;
    }

    private List<Integer> getTwelveCnpjDigits(String cnpj) {
        List<Integer> twelveCnpjDigits = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            twelveCnpjDigits.add(
                    Character.getNumericValue(cnpj.charAt(i))
            );
        }

        return twelveCnpjDigits;
    }

    private List<Integer> getCnpjDigits(String cnpj) {
        List<Integer> cnpjDigits = new ArrayList<>();

        for (char number : cnpj.toCharArray()) {
            cnpjDigits.add(Character.getNumericValue(number));
        }

        return cnpjDigits;
    }

    private int getVerificationDigit(
            List<Integer> cnpjIntegersList,
            boolean isTheFirstDigit
    ) {
        int multiplierResult =
                getMultiplierResult(cnpjIntegersList, isTheFirstDigit);

        int remainderResult = multiplierResult % 11;

        if (remainderResult < 2) {
            return 0;
        }

        return 11 - remainderResult;
    }

    private int getMultiplierResult(
            List<Integer> cnpjIntegersList,
            boolean isTheFirstDigit
    ) {
        int multiplierResult = 0;

        int cnpjNumberLimit = isTheFirstDigit ? 12 : 13;

        int multiplier = isTheFirstDigit ? 5 : 6;

        for (
                int cnpjNumberIterator = 0;
                cnpjNumberIterator < cnpjNumberLimit;
                cnpjNumberIterator++
        ) {
            Integer integer = cnpjIntegersList.get(cnpjNumberIterator);

            multiplierResult += integer * multiplier;

            multiplier--;

            if (multiplier == 1) {
                multiplier = 9;
            }
        }

        return multiplierResult;
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    private boolean isRepeatedNumbers(String cnpj) {
        return cnpj.matches("(\\d)\\1{13}");
    }
}