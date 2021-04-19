CREATE TABLE `mm`.`log` (
  `idlog` INT NOT NULL AUTO_INCREMENT,
  `operacao` VARCHAR(45) NOT NULL,
  `momento` TIMESTAMP NOT NULL,
  `descricao` MEDIUMTEXT NOT NULL,
  `usuario` TINYINT NOT NULL,
  PRIMARY KEY (`idlog`));
 
/*Triggers Classificação*/
DROP TRIGGER IF EXISTS `mm`.`classificacao_AFTER_INSERT`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mm`.`classificacao_AFTER_INSERT` AFTER INSERT ON `classificacao` FOR EACH ROW
BEGIN
	INSERT INTO mm.log values (default, 'Inserção', now(), concat("Registrada nova classificação <",new.nome, "> com id <", new.idclassificacao, ">"), 0);
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `mm`.`classificacao_AFTER_UPDATE`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mm`.`classificacao_AFTER_UPDATE` AFTER UPDATE ON `classificacao` FOR EACH ROW
BEGIN
	INSERT INTO mm.log values (default, 'Atualização', now(), concat("Classificação atualizada de <",old.nome, "> para <", new.nome, "> com id <", new.idclassificacao, ">"), 0);
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `mm`.`classificacao_AFTER_DELETE`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER=`root`@`localhost` TRIGGER `classificacao_AFTER_DELETE` AFTER DELETE ON `classificacao` FOR EACH ROW BEGIN
	INSERT INTO mm.log values (default, 'Exclusão', now(), concat("Classificação excluída: <", old.nome, "> com id <", old.idclassificacao, ">"), 0);
END$$
DELIMITER ;

/*Triggers Instituição*/
DROP TRIGGER IF EXISTS `mm`.`instituicao_AFTER_INSERT`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER=`root`@`localhost` TRIGGER `instituicao_AFTER_INSERT` AFTER INSERT ON `instituicao` FOR EACH ROW BEGIN
	INSERT INTO mm.log values (default, 'Inserção', now(), concat("Registrada nova instituição <",new.nome, ">, saldo <R$ ", new.saldo, ">, tipo <", new.idtipo, ">, id <", new.idinstituicao, ">"), new.idusuario);
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `mm`.`instituicao_AFTER_UPDATE`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER=`root`@`localhost` TRIGGER `instituicao_AFTER_UPDATE` AFTER UPDATE ON `instituicao` FOR EACH ROW BEGIN
	INSERT INTO mm.log values (default, 'Atualização', now(), concat("Instituição atualizada de nome <", old.nome, ">, saldo <", old.saldo, ">, tipo <", old.idtipo, "> para nome <", new.nome, ">, saldo <", new.saldo, ">, tipo <", new.idtipo, ">, com id <", new.idinstituicao, ">"), new.idusuario);
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `mm`.`instituicao_AFTER_DELETE`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER=`root`@`localhost` TRIGGER `instituicao_AFTER_DELETE` AFTER DELETE ON `instituicao` FOR EACH ROW BEGIN
	INSERT INTO mm.log values (default, 'Exclusão', now(), concat("Instituição excluída nome <", old.nome, ">, saldo <", old.saldo, ">, tipo <", old.idtipo, "> com id <", old.idinstituicao, ">"), old.idusuario);
END$$
DELIMITER ;

/*Quem/Pessoa*/
DROP TRIGGER IF EXISTS `mm`.`quem_AFTER_INSERT`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mm`.`quem_AFTER_INSERT` AFTER INSERT ON `quem` FOR EACH ROW
BEGIN
	INSERT INTO mm.log values (
		default, 
		'Inserção', 
		now(), 
		concat("Registrada nova pessoa <", new.nome, ">, saldo <R$ ", new.saldo, ">, id <", new.idquem, ">"), 
		new.idusuario
    );
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `mm`.`quem_AFTER_UPDATE`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mm`.`quem_AFTER_UPDATE` AFTER UPDATE ON `quem` FOR EACH ROW
BEGIN
	INSERT INTO mm.log values (
		default, 
        'Atualização', 
        now(), 
        concat("Pessoa atualizada de nome <", old.nome, ">, saldo <", old.saldo, "> para nome <", new.nome, ">, saldo <", new.saldo, "> com id <", new.idquem, ">"), 
        new.idusuario
	);
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `mm`.`quem_AFTER_DELETE`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mm`.`quem_AFTER_DELETE` AFTER DELETE ON `quem` FOR EACH ROW
BEGIN
	INSERT INTO mm.log values (
		default, 
        'Exclusão', 
        now(), 
        concat("Pessoa excluída nome <", old.nome, ">, saldo <", old.saldo, "> com id <", old.idquem, ">"), 
        old.idusuario
	);
END$$
DELIMITER ;

/*Registros*/
DROP TRIGGER IF EXISTS `mm`.`registro_AFTER_INSERT`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mm`.`registro_AFTER_INSERT` AFTER INSERT ON `registro` FOR EACH ROW
BEGIN
	INSERT INTO mm.log values (
		default, 
		'Inserção', 
		now(), 
		concat("Registrada lançamento idtipo <", new.idtipo, ">, valor <R$ ", new.valor, ">, descrição <", new.descricao, ">, data <", new.data, ">, pessoa <", new.idquem, ">, classificação <", new.idclassificacao, "> com id <", new.idregistro, ">"), 
		new.idusuario
    );
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `mm`.`registro_AFTER_UPDATE`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mm`.`registro_AFTER_UPDATE` AFTER UPDATE ON `registro` FOR EACH ROW
BEGIN
	INSERT INTO mm.log values (
		default, 
        'Atualização', 
        now(), 
        concat("Lançamento atualizado de idtipo <", old.idtipo, ">, valor <R$ ", old.valor, ">, descrição <", old.descricao, ">, data <", old.data, ">, pessoa <", old.idquem, ">, classificação <", old.idclassificacao, ">
        para idtipo <", new.idtipo, ">, valor <R$ ", new.valor, ">, descrição <", new.descricao, ">, data <", new.data, ">, pessoa <", new.idquem, ">, classificação <", new.idclassificacao, "> com id <", new.idregistro, ">"),
        new.idusuario
	);
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `mm`.`registro_AFTER_DELETE`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mm`.`registro_AFTER_DELETE` AFTER DELETE ON `registro` FOR EACH ROW
BEGIN
	INSERT INTO mm.log values (
		default, 
        'Exclusão', 
        now(), 
        concat("Lançamento excluído idtipo <", old.idtipo, ">, valor <R$ ", old.valor, ">, descrição <", old.descricao, ">, data <", old.data, ">, pessoa <", old.idquem, ">, classificação <", old.idclassificacao, ">"),
        old.idusuario
	);
END$$
DELIMITER ;

/*Usuário
DROP TRIGGER IF EXISTS `mm`.`usuario_AFTER_UPDATE`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mm`.`usuario_AFTER_UPDATE` AFTER UPDATE ON `usuario` FOR EACH ROW
BEGIN
	INSERT INTO mm.log values (
		default, 
        'Atualização', 
        now(), 
        "Usuário realizou atualização nos seus dados",
        new.idusuario
	);
END$$
DELIMITER ;
*/
DROP TRIGGER IF EXISTS `mm`.`usuario_AFTER_UPDATE`;

DELIMITER $$
USE `mm`$$
CREATE DEFINER=`root`@`localhost` TRIGGER `usuario_AFTER_UPDATE` AFTER UPDATE ON `usuario` FOR EACH ROW BEGIN
	set @mensagem = "Realizada alteração no(s) dado(s) do usuário: ";
    if(old.senha != new.senha) THEN
		set @mensagem = concat(@mensagem, "<senha alterada> ");
    end if;
	if(old.salario != new.salario) THEN
		set @mensagem = concat(@mensagem, "<salário alterado de ", old.salario, " para ", new.salario, "> ");
    end if;
    if(old.tema != new.tema) THEN
		set @mensagem = concat(@mensagem, "<tema preferido atualizado para ", new.tema, ">");
    end if;
    if(@mensagem != "Realizada alteração no(s) dado(s) do usuário: ") THEN
		INSERT INTO mm.log values (
			default, 
			'Atualização', 
			now(), 
			@mensagem,
			new.idusuario
		);
	end if;
END$$
DELIMITER ;

