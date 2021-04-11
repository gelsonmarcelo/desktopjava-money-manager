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
