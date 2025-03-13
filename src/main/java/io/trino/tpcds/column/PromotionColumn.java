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

import static io.trino.tpcds.Table.PROMOTION;
import static io.trino.tpcds.column.ColumnTypes.DECIMAL;
import static io.trino.tpcds.column.ColumnTypes.IDENTIFIER;
import static io.trino.tpcds.column.ColumnTypes.INTEGER;
import static io.trino.tpcds.column.ColumnTypes.character;
import static io.trino.tpcds.column.ColumnTypes.varchar;

public enum PromotionColumn
        implements Column
{
    P_PROMO_SK(IDENTIFIER),
    P_PROMO_ID(character(16)),
    P_START_DATE_SK(IDENTIFIER),
    P_END_DATE_SK(IDENTIFIER),
    P_ITEM_SK(IDENTIFIER),
    P_PROMO_NAME(character(50)),
    P_CHANNEL_DETAILS(varchar(100)),
    P_PURPOSE(character(15)),
    P_RESPONSE_TARGET(INTEGER),
    P_CHANNEL_DMAIL(INTEGER),
    P_CHANNEL_EMAIL(INTEGER),
    P_CHANNEL_CATALOG(INTEGER),
    P_CHANNEL_TV(INTEGER),
    P_CHANNEL_RADIO(INTEGER),
    P_CHANNEL_PRESS(INTEGER),
    P_CHANNEL_EVENT(INTEGER),
    P_CHANNEL_DEMO(INTEGER),
    P_DISCOUNT_ACTIVE(INTEGER),
    P_COST_NUMBER(DECIMAL),
    P_COST_PRECISION(INTEGER),
    P_COST_SCALE(INTEGER),
    P_COST_FLAGS(INTEGER);

    private final ColumnType type;

    PromotionColumn(ColumnType type)
    {
        this.type = type;
    }

    @Override
    public Table getTable()
    {
        return PROMOTION;
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
