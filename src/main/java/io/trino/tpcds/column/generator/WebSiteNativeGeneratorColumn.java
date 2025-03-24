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

import static io.trino.tpcds.Table.WEB_SITE;
import static io.trino.tpcds.column.ColumnTypes.DATE;
import static io.trino.tpcds.column.ColumnTypes.DECIMAL;
import static io.trino.tpcds.column.ColumnTypes.IDENTIFIER;
import static io.trino.tpcds.column.ColumnTypes.INTEGER;
import static io.trino.tpcds.column.ColumnTypes.character;
import static io.trino.tpcds.column.ColumnTypes.varchar;

public enum WebSiteNativeGeneratorColumn
        implements Column
{
    WEB_SITE_SK(IDENTIFIER),
    WEB_SITE_ID(character(16)),
    WEB_REC_START_DATE(DATE),
    WEB_REC_END_DATE(DATE),
    WEB_NAME(varchar(50)),
    WEB_OPEN_DATE_SK(DATE),
    WEB_CLOSE_DATE_SK(DATE),
    WEB_CLASS(varchar(50)),
    WEB_MANAGER(varchar(40)),
    WEB_MARKET_CLASS(varchar(50)),
    WEB_MARKET_DESC(varchar(100)),
    WEB_MARKET_MANAGER(varchar(40)),
    WEB_COMPANY_NAME(character(50)),
    WEB_MARKET_ID(INTEGER),
    WEB_COMPANY_ID(INTEGER),
    WEB_STREET_NAME(varchar(60)),
    WEB_STREET_NAME2(varchar(60)),
    WEB_STREET_TYPE(character(15)),
    WEB_SUITE_NUMBER(character(10)),
    WEB_CITY(varchar(60)),
    WEB_COUNTY(varchar(30)),
    WEB_STATE(character(2)),
    WEB_COUNTRY(varchar(20)),
    WEB_STREET_NUMBER(INTEGER),
    WEB_ZIP(INTEGER),
    WEB_GMT_OFFSET(INTEGER),
    WEB_PADDING1(INTEGER),
    WEB_TAX_PERCENTAGE_NUMBER(DECIMAL),
    WEB_TAX_PERCENTAGE_PRECISION(INTEGER),
    WEB_TAX_PERCENTAGE_SCALE(INTEGER),
    WEB_TAX_PERCENTAGE_FLAGS(INTEGER),
    WEB_PADDING2(INTEGER);

    private final ColumnType type;

    WebSiteNativeGeneratorColumn(ColumnType type)
    {
        this.type = type;
    }

    @Override
    public Table getTable()
    {
        return WEB_SITE;
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
