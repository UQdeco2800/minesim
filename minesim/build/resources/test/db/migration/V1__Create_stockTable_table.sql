CREATE TABLE stockTable (
                ID INT NOT NULL,
                height INT NOT NULL,
                width INT NOT NULL,
                itemName VARCHAR(50) NOT NULL,
                stock INT NOT NULL,
                stack INT NOT NULL,
                peonClass VARCHAR(50),
                itemClass VARCHAR(50),
                itemType VARCHAR(50) ,
                PRIMARY KEY (itemName, ID)
);


INSERT INTO stockTable(ID,  height, width, itemName, stock, stack, peonClass, itemClass, itemType) VALUES (1, 20, 20, 'Sword', 5, 99, 'all', 'weapon', null);
INSERT INTO stockTable(ID,  height, width, itemName, stock, stack, peonClass, itemClass, itemType) VALUES (3, 20, 20, 'Pickaxe', 20, 99, 'all', 'tool', null);
INSERT INTO stockTable(ID,  height, width, itemName, stock, stack, peonClass, itemClass, itemType) VALUES (4, 20, 20, 'Hammer', 8, 99, 'all', 'tool', null);
INSERT INTO stockTable(ID,  height, width, itemName, stock, stack, peonClass, itemClass, itemType) VALUES (5, 20, 20, 'Shovel', 10, 99, 'all', 'tool', null);
INSERT INTO stockTable(ID,  height, width, itemName, stock, stack, peonClass, itemClass, itemType) VALUES (6, 20, 20, 'Diamond', 0, 99, 'all', 'minedEntity', null);
INSERT INTO stockTable(ID,  height, width, itemName, stock, stack, peonClass, itemClass, itemType) VALUES (7, 20, 20, 'Iron', 0, 99, 'all', 'minedEntity', null);
INSERT INTO stockTable(ID,  height, width, itemName, stock, stack, peonClass, itemClass, itemType) VALUES (8, 20, 20, 'Gold', 0, 99, 'all', 'minedEntity', null);
INSERT INTO stockTable(ID,  height, width, itemName, stock, stack, peonClass, itemClass, itemType) VALUES (9, 20, 20, 'Copper', 0, 99, 'all', 'minedEntity', null);
INSERT INTO stockTable(ID,  height, width, itemName, stock, stack, peonClass, itemClass, itemType) VALUES (10, 20, 20, 'Ruby', 0, 99, 'all', 'minedEntity', null);
INSERT INTO stockTable(ID,  height, width, itemName, stock, stack, peonClass, itemClass, itemType) VALUES (11, 20, 20, 'Stone', 0, 99, 'all', 'minedEntity', null);
INSERT INTO stockTable(ID,  height, width, itemName, stock, stack, peonClass, itemClass, itemType) VALUES (12, 20, 20, 'Sapphire', 0, 99, 'all', 'minedEntity', null);


