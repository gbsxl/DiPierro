package Di.Pierro.domain.enums;

public enum AssociationType {
    // ==========================================
    // VÍNCULOS SOCIETÁRIOS E CORPORATIVOS
    // Foco: Estruturas empresariais e controle
    // ==========================================
    SOCIO,
    SOCIO_ADMINISTRADOR,
    DIRETOR,
    CONSELHEIRO,                  // Membro do conselho administrativo
    REPRESENTANTE_LEGAL,
    CONTROLADORA,                 // Empresa Holding que controla outra
    SUBSIDIARIA,                  // Empresa controlada por uma Holding

    // ==========================================
    // VÍNCULOS FAMILIARES (Rede de Laranjas)
    // Foco: Ocultação de patrimônio próximo
    // ==========================================
    CONJUGE,
    PAI_MAE,
    FILHO_FILHA,
    IRMAO_IRMA,
    PARENTE_SECUNDARIO,           // Primos, tios, sobrinhos (afastados, mas úteis na malha)

    // ==========================================
    // VÍNCULOS TRABALHISTAS E FINANCEIROS
    // Foco: Subordinação e dependência financeira
    // ==========================================
    EMPREGADOR,
    EMPREGADO,
    PRESTADOR_SERVICO,            // Contratos PJ/Terceirizados
    PROCURADOR,                   // ALERTA ALTO: Quem assina cheques em nome de outro
    FIADOR,                       // Quem garante dívidas de terceiros

    // ==========================================
    // VÍNCULOS INDIRETOS / OSINT PURO
    // Foco: Conexões ocultas e anomalias físicas
    // ==========================================
    COMPARTILHA_ENDERECO,         // ALERTA ALTO: 5 empresas diferentes na mesma sala comercial
    COMPARTILHA_CONTATO,          // Empresas de donos diferentes usando o mesmo telefone/contador
    DOADOR_CAMPANHA,              // Vínculo financeiro eleitoral
    ASSESSOR_POLITICO,            // Vínculo de subordinação em gabinete público

    // ==========================================
    // VÍNCULO NÃO DEFINIDO
    // Foco: mapear sem ter uma confirmação direta
    // ==========================================
    LIGACAO_SEM_CLASSIFICACAO
}