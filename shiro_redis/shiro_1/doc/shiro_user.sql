/************************************************************分割线************************************************************/
/* todo shiro_user */

CREATE TABLE `shiro_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `user_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `shiro_user` VALUES (1, '5f2fc818-c4a1-11e9-8b6e-94c6910c8b5c', 'user1', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `shiro_user` VALUES (2, '6a53ad1c-c4a1-11e9-a589-94c6910c8b5c', 'user2', 'e10adc3949ba59abbe56e057f20f883e');
INSERT INTO `shiro_user` VALUES (3, '72311998-c4a1-11e9-a09a-94c6910c8b5c', 'user3', 'e10adc3949ba59abbe56e057f20f883e');