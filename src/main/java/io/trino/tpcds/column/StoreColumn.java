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

import static io.trino.tpcds.Table.STORE;
import static io.trino.tpcds.column.ColumnTypes.DATE;
import static io.trino.tpcds.column.ColumnTypes.DECIMAL;
import static io.trino.tpcds.column.ColumnTypes.IDENTIFIER;
import static io.trino.tpcds.column.ColumnTypes.INTEGER;
import static io.trino.tpcds.column.ColumnTypes.character;
import static io.trino.tpcds.column.ColumnTypes.varchar;

public enum StoreColumn
        implements Column
{
    S_STORE_SK(IDENTIFIER),
    S_STORE_ID(character(16)),
    S_REC_START_DATE(DATE),
    S_REC_END_DATE(DATE),
    S_CLOSED_DATE_SK(DATE),
    S_STORE_NAME(varchar(50)),
    S_HOURS(character(20)),
    S_MANAGER(varchar(40)),
    S_GEOGRAPHY_CLASS(varchar(100)),
    S_MARKET_DESC(varchar(100)),
    S_MARKET_MANAGER(varchar(40)),
    S_DIVISION_ID(IDENTIFIER),
    S_DIVISION_NAME(varchar(50)),
    S_COMPANY_ID(IDENTIFIER),
    S_COMPANY_NAME(varchar(50)),
    S_NUMBER_EMPLOYEES(INTEGER),
    S_FLOOR_SPACE(INTEGER),
    S_MARKET_ID(INTEGER),
    S_PADDING1(INTEGER),
    S_TAX_PRECENTAGE_NUMBER(DECIMAL),
    S_TAX_PRECENTAGE_PRECISION(INTEGER),
    S_TAX_PRECENTAGE_SCALE(INTEGER),
    S_TAX_PRECENTAGE_FLAGS(INTEGER),
    S_PADDING2(INTEGER),
    S_STREET_NAME(varchar(60)),
    S_STREET_NAME2(varchar(60)),
    S_STREET_TYPE(character(15)),
    S_SUITE_NUMBER(character(10)),
    S_CITY(varchar(60)),
    S_COUNTY(varchar(30)),
    S_STATE(character(2)),
    S_COUNTRY(varchar(20)),
    S_STREET_NUMBER(INTEGER),
    S_ZIP(INTEGER),
    S_GMT_OFFSET(INTEGER),
    S_PADDING3(INTEGER);

    private final ColumnType type;

    StoreColumn(ColumnType type)
    {
        this.type = type;
    }

    @Override
    public Table getTable()
    {
        return STORE;
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
