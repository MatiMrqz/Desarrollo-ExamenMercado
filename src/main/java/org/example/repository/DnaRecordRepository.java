package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entity.DnaRecord;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DnaRecordRepository {
    private DnaRecord dnaRecord;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("DnaRecordPU");
    private EntityManager em = emf.createEntityManager();

    public DnaRecord findByHash(String[] dna) {
        String id = generateSha256(dna);
        return em.createQuery("SELECT d FROM DnaRecord d WHERE d.hash = :hash", DnaRecord.class)
                .setParameter("hash", id)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
    private String generateSha256(String[] dna) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            StringBuilder dnaString = new StringBuilder();
            for (String strand : dna) {
                dnaString.append(strand);
            }
            byte[] hashBytes = digest.digest(dnaString.toString().getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDnaRecord(String[] dna, boolean isMutant) {
        String hash = generateSha256(dna);
        DnaRecord record = DnaRecord.builder()
                .hash(hash)
                .isMutant(isMutant)
                .build();
        em.getTransaction().begin();
        em.persist(record);
        em.getTransaction().commit();
    }

    public int getMutantCount() {
        Long count = (Long) em.createQuery("SELECT COUNT(d) FROM DnaRecord d WHERE d.isMutant = true")
                .getSingleResult();
        return count.intValue();
    }

    public int getHumanCount() {
        Long count = (Long) em.createQuery("SELECT COUNT(d) FROM DnaRecord d WHERE d.isMutant = false")
                .getSingleResult();
        return count.intValue();
    }
}
