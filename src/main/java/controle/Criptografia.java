package controle;

import java.security.MessageDigest; // classe do Java para gerar o hash
import java.security.NoSuchAlgorithmException; // exceção se o algoritmo não existir

// Classe responsável SOMENTE por transformar texto em hash (senha segura)
public class Criptografia {

    // Recebe um texto (a senha) e devolve o hash SHA-256 dele
    public static String hash(String senha) {
        try {
            // 1. Obtém o algoritmo de hash SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 2. Calcula o hash dos bytes da string (o "resumo" da senha)
            byte[] digest = md.digest(senha.getBytes());

            // 3. Converte o array de bytes em texto hexadecimal (64 caracteres)
            StringBuilder sb = new StringBuilder(); // monta a string aos poucos
            for (byte b : digest) { // para cada byte do hash...
                sb.append(String.format("%02x", 0xFF & b)); // ...vira 2 caracteres hexadecimais
            }
            return sb.toString(); // devolve o hash pronto (ex.: 240be5...)
        } catch (NoSuchAlgorithmException e) { // se o Java não tiver o algoritmo...
            throw new RuntimeException("Algoritmo SHA-256 não encontrado", e); // ...erro
        }
    }
}
