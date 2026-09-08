package Di.Pierro.application.validators.cpf;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class CpfValidator implements ConstraintValidator<CPF, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return validatorCpf(value);
    }

    public boolean validatorCpf(String cpf) {
        cpf = cpf.replaceAll("\\D", "");

        boolean cpfHasCorrectLength = cpf.length() == 11;
        boolean cpfIsIncompleteButRight = cpfHasCorrectLength && cpf.contains("*");
        boolean cpfIsComplete = cpfHasCorrectLength && isNumeric(cpf) && !isRepeatedNumbers(cpf);

        if (cpfIsIncompleteButRight) {
            return true;
        }

        if (cpfIsComplete) {
            List<Integer> cpfIntegersList = getNineCpfDigits(cpf);
            List<Integer> cpfToCompare = getCpfDigits(cpf);

            int firstVerificationDigit = getVerificatorDigit(cpfIntegersList, true);
            cpfIntegersList.add(firstVerificationDigit);

            int secondVerificationDigit = getVerificatorDigit(cpfIntegersList, false);
            cpfIntegersList.add(secondVerificationDigit);

            return areEqual(cpfIntegersList, cpfToCompare);
        }

        return false;
    }

    private boolean areEqual(List<Integer> cpfIntegersList, List<Integer> cpfToCompare) {
        if (cpfIntegersList.size() != cpfToCompare.size()) {
            return false;
        }
        for (int i = 0; i < cpfToCompare.size(); i++) {
            if (!cpfToCompare.get(i).equals(cpfIntegersList.get(i))) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> getNineCpfDigits(String cpf) {
        List<Integer> nineCpfDigits = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            nineCpfDigits.add(Character.getNumericValue(cpf.charAt(i)));
        }
        return nineCpfDigits;
    }

    private List<Integer> getCpfDigits(String cpf) {
        List<Integer> cpfDigits = new ArrayList<>();
        for (char number : cpf.toCharArray()) {
            cpfDigits.add(Character.getNumericValue(number));
        }
        return cpfDigits;
    }

    private int getVerificatorDigit(List<Integer> cpfIntegersList, boolean isTheFirstDigit) {
        int multiplierResult = getMultiplierResult(cpfIntegersList, isTheFirstDigit);
        int remainderResult = multiplierResult % 11;

        if (remainderResult < 2) {
            return 0;
        }

        return 11 - remainderResult;
    }

    private int getMultiplierResult(List<Integer> cpfIntegersList, boolean isTheFirstDigit) {
        int multiplierResult = 0;
        int cpfNumberLimit = isTheFirstDigit ? 9 : 10;
        int multiplier = isTheFirstDigit ? 10 : 11;

        for (int cpfNumberIterator = 0, multiplierIterator = multiplier; cpfNumberIterator < cpfNumberLimit; cpfNumberIterator++, multiplierIterator--) {
            Integer integer = cpfIntegersList.get(cpfNumberIterator);
            multiplierResult = multiplierResult + (integer * multiplierIterator);
        }

        return multiplierResult;
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    private boolean isRepeatedNumbers(String cpf) {
        return cpf.matches("(\\d)\\1{10}");
    }
}
