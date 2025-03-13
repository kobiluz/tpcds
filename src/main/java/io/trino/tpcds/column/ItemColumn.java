/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.tpcds.column;

import io.trino.tpcds.Table;

import static io.trino.tpcds.Table.ITEM;
import static io.trino.tpcds.column.ColumnTypes.DATE;
import static io.trino.tpcds.column.ColumnTypes.DECIMAL;
import static io.trino.tpcds.column.ColumnTypes.IDENTIFIER;
import static io.trino.tpcds.column.ColumnTypes.INTEGER;
import static io.trino.tpcds.column.ColumnTypes.character;
import static io.trino.tpcds.column.ColumnTypes.varchar;

public enum ItemColumn
        implements Column
{
    I_ITEM_SK(IDENTIFIER),
    I_ITEM_ID(character(16)),
    I_REC_START_DATE(DATE),
    I_REC_END_DATE(DATE),
    I_ITEM_DESC(varchar(200)),
    I_BRAND_ID(IDENTIFIER),
    I_BRAND(character(50)),
    I_CLASS_ID(IDENTIFIER),
    I_CLASS(character(50)),
    I_CATEGORY_ID(IDENTIFIER),
    I_CATEGORY(character(50)),
    I_MANUFACT_ID(IDENTIFIER),
    I_MANUFACT(character(50)),
    I_SIZE(character(20)),
    I_FORMULATION(character(20)),
    I_COLOR(character(20)),
    I_UNITS(character(10)),
    I_CONTAINER(character(10)),
    I_MANAGER_ID(IDENTIFIER),
    I_PRODUCT_NAME(character(50)),
    I_PROMO_SK(IDENTIFIER),
    I_CURRENT_PRICE_NUMBER(DECIMAL),
    I_CURRENT_PRICE_PRECISION(INTEGER),
    I_CURRENT_PRICE_SCALE(INTEGER),
    I_CURRENT_PRICE_FLAGS(INTEGER),
    I_PADDING1(INTEGER),
    I_WHOLESALE_COST_NUMBER(DECIMAL),
    I_WHOLESALE_COST_PRECISION(INTEGER),
    I_WHOLESALE_COST_SCALE(INTEGER),
    I_WHOLESALE_COST_FLAGS(INTEGER),
    I_PADDING2(INTEGER);

    private final ColumnType type;

    ItemColumn(ColumnType type)
    {
        this.type = type;
    }

    @Override
    public Table getTable()
    {
        return ITEM;
    }

    @Override
    public String getName()
    {
        return name().toLowerCase();
    }

    @Override
    public ColumnType getType()
    {
        return type;
    }

    @Override
    public int getPosition()
    {
        return ordinal();
    }
}
