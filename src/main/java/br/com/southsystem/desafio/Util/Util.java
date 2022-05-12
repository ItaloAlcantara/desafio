package br.com.southsystem.desafio.Util;

public class Util {

    public static String formatCpf(String cpf) {
        if(cpf.contains(".") || cpf.contains("/"))
            return cpf.replace(".","").replace("/","");
        return cpf;
    }
}
