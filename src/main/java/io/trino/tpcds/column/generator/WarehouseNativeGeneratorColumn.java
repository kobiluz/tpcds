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
package io.trino.tpcds.column.generator;

import io.trino.tpcds.Table;
import io.trino.tpcds.column.Column;
import io.trino.tpcds.column.ColumnType;

import static io.trino.tpcds.Table.WAREHOUSE;
import static io.trino.tpcds.column.ColumnTypes.IDENTIFIER;
import static io.trino.tpcds.column.ColumnTypes.INTEGER;
import static io.trino.tpcds.column.ColumnTypes.character;
import static io.trino.tpcds.column.ColumnTypes.varchar;

public enum WarehouseNativeGeneratorColumn
        implements Column
{
    W_WAREHOUSE_SK(IDENTIFIER),
    W_WAREHOUSE_ID(character(16)),
    W_WAREHOUSE_NAME(varchar(20)),
    W_WAREHOUSE_SQ_FT(INTEGER),
    W_PADDING1(INTEGER),
    W_STREET_NAME(varchar(60)),
    W_STREET_NAME2(varchar(60)),
    W_STREET_TYPE(character(15)),
    W_SUITE_NUMBER(character(10)),
    W_CITY(varchar(60)),
    W_COUNTY(varchar(30)),
    W_STATE(character(2)),
    W_COUNTRY(varchar(20)),
    W_STREET_NUMBER(INTEGER),
    W_ZIP(INTEGER),
    W_GMT_OFFSET(INTEGER),
    W_PADDING2(INTEGER);

    private final ColumnType type;

    WarehouseNativeGeneratorColumn(ColumnType type)
    {
        this.type = type;
    }

    @Override
    public Table getTable()
    {
        return WAREHOUSE;
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
