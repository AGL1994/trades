CREATE TABLE `time_series_data` (
  `item_id` varchar(50) NOT NULL,
  `stock_code` varchar(50) NOT NULL,
  `trading_date` date NOT NULL,
  `item_valu_one` int(7) DEFAULT NULL,
  `item_value_two` double DEFAULT NULL,
  `item_value_three` double DEFAULT NULL,
  PRIMARY KEY (`stock_code`,`item_id`,`trading_date`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
