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

package io.trino.tpcds.row.generator;

import io.trino.tpcds.Session;
import io.trino.tpcds.row.CatalogSalesRow;
import io.trino.tpcds.row.TableRow;
import io.trino.tpcds.type.Decimal;
import io.trino.tpcds.type.Pricing;

import javax.annotation.concurrent.NotThreadSafe;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SequenceLayout;
import java.lang.foreign.StructLayout;
import java.util.ArrayList;
import java.util.List;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.CATALOG_SALES;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_BILL_ADDR_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_BILL_CDEMO_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_BILL_CUSTOMER_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_BILL_HDEMO_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_CALL_CENTER_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_CATALOG_PAGE_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_ORDER_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_DECIMAL1;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_DECIMAL2;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_DECIMAL3;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_DECIMAL4;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_DECIMAL5;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT1;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT10;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT11;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT12;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT13;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT14;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT15;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT16;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT17;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT2;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT3;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT4;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT5;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT6;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT7;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT8;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PADDING_INT9;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_COUPON_AMT_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_COUPON_AMT_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_COUPON_AMT_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_COUPON_AMT_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_DISCOUNT_AMOUNT_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_DISCOUNT_AMOUNT_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_DISCOUNT_AMOUNT_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_DISCOUNT_AMOUNT_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_LIST_PRICE_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_LIST_PRICE_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_LIST_PRICE_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_LIST_PRICE_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_SALES_PRICE_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_SALES_PRICE_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_SALES_PRICE_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_SALES_PRICE_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_SHIP_COST_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_SHIP_COST_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_SHIP_COST_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_SHIP_COST_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_TAX_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_TAX_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_TAX_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_TAX_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_WHOLESALE_COST_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_WHOLESALE_COST_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_WHOLESALE_COST_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_EXT_WHOLESALE_COST_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_LIST_PRICE_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_LIST_PRICE_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_LIST_PRICE_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_LIST_PRICE_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_SHIP_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_SHIP_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_SHIP_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_SHIP_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_SHIP_TAX_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_SHIP_TAX_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_SHIP_TAX_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_SHIP_TAX_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_TAX_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_TAX_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_TAX_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_INC_TAX_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PAID_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PROFIT_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PROFIT_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PROFIT_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_NET_PROFIT_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_QUANTITY;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_SALES_PRICE_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_SALES_PRICE_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_SALES_PRICE_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_SALES_PRICE_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_SHIP_COST_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_SHIP_COST_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_SHIP_COST_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_SHIP_COST_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_TAX_PCT_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_TAX_PCT_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_TAX_PCT_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_TAX_PCT_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_WHOLESALE_COST_FLAGS;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_WHOLESALE_COST_NUMBER;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_WHOLESALE_COST_PRECISION;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PRICING_WHOLESALE_COST_SCALE;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_PROMO_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_SHIP_ADDR_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_SHIP_CDEMO_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_SHIP_CUSTOMER_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_SHIP_DATE_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_SHIP_HDEMO_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_SHIP_MODE_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_SOLD_DATE_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_SOLD_ITEM_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_SOLD_TIME_SK;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_VALID;
import static io.trino.tpcds.column.CatalogSalesColumn.CS_WAREHOUSE_SK;
import static io.trino.tpcds.generator.CatalogSalesGeneratorColumn.CS_NULLS;

@NotThreadSafe
public class CatalogSalesRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final int MAX_MULTIPLE_GENERATED_ROWS = 15;
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_catalog_sales";
    private final StructLayout csRowLayout;
    private StructLayout crRowLayout;
    private StructLayout rowLayout;
    private SequenceLayout multipleRowsLayout;
    private long currPosition;
    private MemorySegment backupRowSegment;

    public CatalogSalesRowNativeGenerator()
    {
        super(CATALOG_SALES);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);

            csRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(CS_SOLD_DATE_SK.getName()),
                    columnToLayoutMap.get(CS_SOLD_TIME_SK.getName()),
                    columnToLayoutMap.get(CS_SHIP_DATE_SK.getName()),
                    columnToLayoutMap.get(CS_BILL_CUSTOMER_SK.getName()),
                    columnToLayoutMap.get(CS_BILL_CDEMO_SK.getName()),
                    columnToLayoutMap.get(CS_BILL_HDEMO_SK.getName()),
                    columnToLayoutMap.get(CS_BILL_ADDR_SK.getName()),
                    columnToLayoutMap.get(CS_SHIP_CUSTOMER_SK.getName()),
                    columnToLayoutMap.get(CS_SHIP_CDEMO_SK.getName()),
                    columnToLayoutMap.get(CS_SHIP_HDEMO_SK.getName()),
                    columnToLayoutMap.get(CS_SHIP_ADDR_SK.getName()),
                    columnToLayoutMap.get(CS_CALL_CENTER_SK.getName()),
                    columnToLayoutMap.get(CS_CATALOG_PAGE_SK.getName()),
                    columnToLayoutMap.get(CS_SHIP_MODE_SK.getName()),
                    columnToLayoutMap.get(CS_WAREHOUSE_SK.getName()),
                    columnToLayoutMap.get(CS_SOLD_ITEM_SK.getName()),
                    columnToLayoutMap.get(CS_PROMO_SK.getName()),
                    columnToLayoutMap.get(CS_ORDER_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_WHOLESALE_COST_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_WHOLESALE_COST_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_WHOLESALE_COST_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_WHOLESALE_COST_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT1.getName()),
                    columnToLayoutMap.get(CS_PRICING_LIST_PRICE_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_LIST_PRICE_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_LIST_PRICE_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_LIST_PRICE_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT2.getName()),
                    columnToLayoutMap.get(CS_PRICING_SALES_PRICE_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_SALES_PRICE_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_SALES_PRICE_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_SALES_PRICE_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT3.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_DISCOUNT_AMOUNT_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_DISCOUNT_AMOUNT_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_DISCOUNT_AMOUNT_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_DISCOUNT_AMOUNT_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT4.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_SALES_PRICE_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_SALES_PRICE_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_SALES_PRICE_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_SALES_PRICE_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT5.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_WHOLESALE_COST_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_WHOLESALE_COST_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_WHOLESALE_COST_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_WHOLESALE_COST_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT6.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_LIST_PRICE_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_LIST_PRICE_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_LIST_PRICE_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_LIST_PRICE_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT7.getName()),
                    columnToLayoutMap.get(CS_PRICING_TAX_PCT_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_TAX_PCT_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_TAX_PCT_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_TAX_PCT_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT8.getName()),
                    columnToLayoutMap.get(CS_PRICING_COUPON_AMT_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_COUPON_AMT_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_COUPON_AMT_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_COUPON_AMT_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT9.getName()),
                    columnToLayoutMap.get(CS_PRICING_SHIP_COST_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_SHIP_COST_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_SHIP_COST_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_SHIP_COST_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT10.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_SHIP_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_SHIP_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_SHIP_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_SHIP_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT11.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_SHIP_TAX_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_SHIP_TAX_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_SHIP_TAX_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_SHIP_TAX_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT12.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PROFIT_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PROFIT_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PROFIT_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PROFIT_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT13.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT14.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_TAX_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_TAX_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_TAX_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_NET_PAID_INC_TAX_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT15.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_TAX_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_TAX_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_TAX_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_TAX_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT16.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_SHIP_COST_NUMBER.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_SHIP_COST_PRECISION.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_SHIP_COST_SCALE.getName()),
                    columnToLayoutMap.get(CS_PRICING_EXT_SHIP_COST_FLAGS.getName()),
                    columnToLayoutMap.get(CS_PADDING_INT17.getName()),
                    columnToLayoutMap.get(CS_PRICING_QUANTITY.getName()),
                    columnToLayoutMap.get(CS_VALID.getName()),
                    columnToLayoutMap.get(CS_PADDING_DECIMAL1.getName()),
                    columnToLayoutMap.get(CS_PADDING_DECIMAL2.getName()),
                    columnToLayoutMap.get(CS_PADDING_DECIMAL3.getName()),
                    columnToLayoutMap.get(CS_PADDING_DECIMAL4.getName()),
                    columnToLayoutMap.get(CS_PADDING_DECIMAL5.getName()));
        }
        catch (Throwable t) {
            System.err.println("CatalogSalesRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find catalog sales row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        if (multipleRowsLayout == null) {
            createRowLayout(childRowGenerator);
        }

        replaceRowWithSales();
        if (currPosition == 0) {
            generateRow(rowNumber);
            if (nativeInt(CS_VALID) == 0) {
                throw new RuntimeException("no rows after generation");
            }
        }

        CatalogSalesRow catalogSalesRow = new CatalogSalesRow(nativeLong(CS_SOLD_DATE_SK),
                nativeLong(CS_SOLD_TIME_SK),
                nativeLong(CS_SHIP_DATE_SK),
                nativeLong(CS_BILL_CUSTOMER_SK),
                nativeLong(CS_BILL_CDEMO_SK),
                nativeLong(CS_BILL_HDEMO_SK),
                nativeLong(CS_BILL_ADDR_SK),
                nativeLong(CS_SHIP_CUSTOMER_SK),
                nativeLong(CS_SHIP_CDEMO_SK),
                nativeLong(CS_SHIP_HDEMO_SK),
                nativeLong(CS_SHIP_ADDR_SK),
                nativeLong(CS_CALL_CENTER_SK),
                nativeLong(CS_CATALOG_PAGE_SK),
                nativeLong(CS_SHIP_MODE_SK),
                nativeLong(CS_WAREHOUSE_SK),
                nativeLong(CS_SOLD_ITEM_SK),
                nativeLong(CS_PROMO_SK),
                nativeLong(CS_ORDER_NUMBER),
                new Pricing(new Decimal(nativeDecimal(CS_PRICING_WHOLESALE_COST_NUMBER), nativeInt(CS_PRICING_WHOLESALE_COST_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_LIST_PRICE_NUMBER), nativeInt(CS_PRICING_LIST_PRICE_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_SALES_PRICE_NUMBER), nativeInt(CS_PRICING_SALES_PRICE_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_EXT_DISCOUNT_AMOUNT_NUMBER), nativeInt(CS_PRICING_EXT_DISCOUNT_AMOUNT_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_EXT_SALES_PRICE_NUMBER), nativeInt(CS_PRICING_EXT_SALES_PRICE_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_EXT_WHOLESALE_COST_NUMBER), nativeInt(CS_PRICING_EXT_WHOLESALE_COST_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_EXT_LIST_PRICE_NUMBER), nativeInt(CS_PRICING_EXT_LIST_PRICE_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_COUPON_AMT_NUMBER), nativeInt(CS_PRICING_COUPON_AMT_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_NET_PAID_INC_SHIP_NUMBER), nativeInt(CS_PRICING_NET_PAID_INC_SHIP_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_NET_PAID_INC_SHIP_TAX_NUMBER), nativeInt(CS_PRICING_NET_PAID_INC_SHIP_TAX_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_NET_PROFIT_NUMBER), nativeInt(CS_PRICING_NET_PROFIT_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_NET_PAID_NUMBER), nativeInt(CS_PRICING_NET_PAID_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_NET_PAID_INC_TAX_NUMBER), nativeInt(CS_PRICING_NET_PAID_INC_TAX_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_EXT_TAX_NUMBER), nativeInt(CS_PRICING_EXT_TAX_PRECISION)),
                            new Decimal(nativeDecimal(CS_PRICING_EXT_SHIP_COST_NUMBER), nativeInt(CS_PRICING_EXT_SHIP_COST_PRECISION)),
                            nativeInt(CS_PRICING_QUANTITY)),
                createNullBitMap(CATALOG_SALES, getRandomNumberStream(CS_NULLS)));
        restoreRowSegment();

        List<TableRow> generatedRows = new ArrayList<>(2);
        generatedRows.add(catalogSalesRow);
        TableRow catalogReturnsRow = ((CatalogReturnsRowNativeGenerator) childRowGenerator).generateRow(getReturnsRow());
        if (catalogReturnsRow != null) {
            generatedRows.add(catalogReturnsRow);
        }

        currPosition++;
        replaceRowWithSales();
        boolean endOfRow = nativeInt(CS_VALID) == 0;
        restoreRowSegment();
        if (endOfRow) {
            currPosition = 0;
        }
        return new RowGeneratorResult(generatedRows, endOfRow);
    }

    private void createRowLayout(RowGenerator childRowGenerator)
    {
        crRowLayout = ((CatalogReturnsRowNativeGenerator) childRowGenerator).getLayout();
        rowLayout = MemoryLayout.structLayout(csRowLayout.withName("sales"), crRowLayout.withName("returns"));
        multipleRowsLayout = MemoryLayout.sequenceLayout(MAX_MULTIPLE_GENERATED_ROWS, rowLayout);
        allocateRow(multipleRowsLayout.byteSize());
        backupRowSegment = rowSegment;
    }

    private void replaceRowWithSales()
    {
        rowSegment = rowSegment.asSlice(currPosition * rowLayout.byteSize(), rowLayout).asSlice(0, csRowLayout);
    }

    private void restoreRowSegment()
    {
        rowSegment = backupRowSegment;
    }

    private MemorySegment getReturnsRow()
    {
        return rowSegment.asSlice(currPosition * rowLayout.byteSize(), rowLayout).asSlice(csRowLayout.byteSize(), crRowLayout);
    }
}
