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
import io.trino.tpcds.row.TableRow;
import io.trino.tpcds.row.WebSalesRow;
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
import static io.trino.tpcds.Table.WEB_SALES;
import static io.trino.tpcds.TableGenerator.nativeMakeRowMethod;
import static io.trino.tpcds.column.WebSalesColumn.WS_BILL_ADDR_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_BILL_CDEMO_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_BILL_CUSTOMER_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_BILL_HDEMO_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_ITEM_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_ORDER_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_DECIMAL1;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_DECIMAL2;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_DECIMAL3;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_DECIMAL4;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_DECIMAL5;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT1;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT10;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT11;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT12;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT13;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT14;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT15;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT16;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT17;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT2;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT3;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT4;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT5;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT6;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT7;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT8;
import static io.trino.tpcds.column.WebSalesColumn.WS_PADDING_INT9;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_COUPON_AMT_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_COUPON_AMT_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_COUPON_AMT_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_COUPON_AMT_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_DISCOUNT_AMOUNT_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_DISCOUNT_AMOUNT_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_DISCOUNT_AMOUNT_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_DISCOUNT_AMOUNT_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_LIST_PRICE_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_LIST_PRICE_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_LIST_PRICE_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_LIST_PRICE_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_SALES_PRICE_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_SALES_PRICE_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_SALES_PRICE_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_SALES_PRICE_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_SHIP_COST_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_SHIP_COST_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_SHIP_COST_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_SHIP_COST_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_TAX_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_TAX_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_TAX_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_TAX_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_WHOLESALE_COST_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_WHOLESALE_COST_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_WHOLESALE_COST_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_EXT_WHOLESALE_COST_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_LIST_PRICE_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_LIST_PRICE_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_LIST_PRICE_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_LIST_PRICE_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_SHIP_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_SHIP_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_SHIP_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_SHIP_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_SHIP_TAX_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_SHIP_TAX_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_SHIP_TAX_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_SHIP_TAX_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_TAX_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_TAX_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_TAX_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_INC_TAX_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PAID_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PROFIT_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PROFIT_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PROFIT_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_NET_PROFIT_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_QUANTITY;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_SALES_PRICE_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_SALES_PRICE_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_SALES_PRICE_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_SALES_PRICE_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_SHIP_COST_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_SHIP_COST_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_SHIP_COST_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_SHIP_COST_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_TAX_PCT_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_TAX_PCT_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_TAX_PCT_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_TAX_PCT_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_WHOLESALE_COST_FLAGS;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_WHOLESALE_COST_NUMBER;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_WHOLESALE_COST_PRECISION;
import static io.trino.tpcds.column.WebSalesColumn.WS_PRICING_WHOLESALE_COST_SCALE;
import static io.trino.tpcds.column.WebSalesColumn.WS_PROMO_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_SHIP_ADDR_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_SHIP_CDEMO_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_SHIP_CUSTOMER_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_SHIP_DATE_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_SHIP_HDEMO_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_SHIP_MODE_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_SOLD_DATE_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_SOLD_TIME_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_VALID;
import static io.trino.tpcds.column.WebSalesColumn.WS_WAREHOUSE_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_WEB_PAGE_SK;
import static io.trino.tpcds.column.WebSalesColumn.WS_WEB_SITE_SK;
import static io.trino.tpcds.generator.WebSalesGeneratorColumn.WS_NULLS;

@NotThreadSafe
public class WebSalesRowNativeGenerator
        extends AbstractRowGenerator
{
    private static final int MAX_MULTIPLE_GENERATED_ROWS = 17;
    private static final String MAKE_ROW_METHOD_NAME = "mk_w_web_sales";
    private final StructLayout wsRowLayout;
    private StructLayout wrRowLayout;
    private StructLayout rowLayout;
    private SequenceLayout multipleRowsLayout;
    private long currPosition;
    private MemorySegment backupRowSegment;

    public WebSalesRowNativeGenerator()
    {
        super(WEB_SALES);

        try {
            generateRowMethod = nativeMakeRowMethod(MAKE_ROW_METHOD_NAME);
            wsRowLayout = MemoryLayout.structLayout(
                    columnToLayoutMap.get(WS_SOLD_DATE_SK.getName()),
                    columnToLayoutMap.get(WS_SOLD_TIME_SK.getName()),
                    columnToLayoutMap.get(WS_SHIP_DATE_SK.getName()),
                    columnToLayoutMap.get(WS_ITEM_SK.getName()),
                    columnToLayoutMap.get(WS_BILL_CUSTOMER_SK.getName()),
                    columnToLayoutMap.get(WS_BILL_CDEMO_SK.getName()),
                    columnToLayoutMap.get(WS_BILL_HDEMO_SK.getName()),
                    columnToLayoutMap.get(WS_BILL_ADDR_SK.getName()),
                    columnToLayoutMap.get(WS_SHIP_CUSTOMER_SK.getName()),
                    columnToLayoutMap.get(WS_SHIP_CDEMO_SK.getName()),
                    columnToLayoutMap.get(WS_SHIP_HDEMO_SK.getName()),
                    columnToLayoutMap.get(WS_SHIP_ADDR_SK.getName()),
                    columnToLayoutMap.get(WS_WEB_PAGE_SK.getName()),
                    columnToLayoutMap.get(WS_WEB_SITE_SK.getName()),
                    columnToLayoutMap.get(WS_SHIP_MODE_SK.getName()),
                    columnToLayoutMap.get(WS_WAREHOUSE_SK.getName()),
                    columnToLayoutMap.get(WS_PROMO_SK.getName()),
                    columnToLayoutMap.get(WS_ORDER_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_WHOLESALE_COST_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_WHOLESALE_COST_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_WHOLESALE_COST_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_WHOLESALE_COST_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT1.getName()),
                    columnToLayoutMap.get(WS_PRICING_LIST_PRICE_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_LIST_PRICE_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_LIST_PRICE_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_LIST_PRICE_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT2.getName()),
                    columnToLayoutMap.get(WS_PRICING_SALES_PRICE_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_SALES_PRICE_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_SALES_PRICE_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_SALES_PRICE_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT3.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_DISCOUNT_AMOUNT_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_DISCOUNT_AMOUNT_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_DISCOUNT_AMOUNT_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_DISCOUNT_AMOUNT_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT4.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_SALES_PRICE_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_SALES_PRICE_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_SALES_PRICE_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_SALES_PRICE_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT5.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_WHOLESALE_COST_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_WHOLESALE_COST_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_WHOLESALE_COST_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_WHOLESALE_COST_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT6.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_LIST_PRICE_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_LIST_PRICE_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_LIST_PRICE_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_LIST_PRICE_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT7.getName()),
                    columnToLayoutMap.get(WS_PRICING_TAX_PCT_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_TAX_PCT_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_TAX_PCT_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_TAX_PCT_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT8.getName()),
                    columnToLayoutMap.get(WS_PRICING_COUPON_AMT_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_COUPON_AMT_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_COUPON_AMT_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_COUPON_AMT_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT9.getName()),
                    columnToLayoutMap.get(WS_PRICING_SHIP_COST_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_SHIP_COST_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_SHIP_COST_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_SHIP_COST_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT10.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_SHIP_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_SHIP_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_SHIP_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_SHIP_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT11.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_SHIP_TAX_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_SHIP_TAX_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_SHIP_TAX_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_SHIP_TAX_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT12.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PROFIT_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PROFIT_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PROFIT_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PROFIT_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT13.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT14.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_TAX_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_TAX_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_TAX_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_NET_PAID_INC_TAX_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT15.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_TAX_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_TAX_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_TAX_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_TAX_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT16.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_SHIP_COST_NUMBER.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_SHIP_COST_PRECISION.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_SHIP_COST_SCALE.getName()),
                    columnToLayoutMap.get(WS_PRICING_EXT_SHIP_COST_FLAGS.getName()),
                    columnToLayoutMap.get(WS_PADDING_INT17.getName()),
                    columnToLayoutMap.get(WS_PRICING_QUANTITY.getName()),
                    columnToLayoutMap.get(WS_VALID.getName()),
                    columnToLayoutMap.get(WS_PADDING_DECIMAL1.getName()),
                    columnToLayoutMap.get(WS_PADDING_DECIMAL2.getName()),
                    columnToLayoutMap.get(WS_PADDING_DECIMAL3.getName()),
                    columnToLayoutMap.get(WS_PADDING_DECIMAL4.getName()),
                    columnToLayoutMap.get(WS_PADDING_DECIMAL5.getName()));
        }
        catch (Throwable t) {
            System.err.println("WebSalesRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find web sales row generator method", t);
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
            if (nativeInt(WS_VALID) == 0) {
                throw new RuntimeException("no rows after generation");
            }
        }

        WebSalesRow webSalesRow = new WebSalesRow(createNullBitMap(WEB_SALES, getRandomNumberStream(WS_NULLS)),
                nativeLong(WS_SOLD_DATE_SK),
                nativeLong(WS_SOLD_TIME_SK),
                nativeLong(WS_SHIP_DATE_SK),
                nativeLong(WS_ITEM_SK),
                nativeLong(WS_BILL_CUSTOMER_SK),
                nativeLong(WS_BILL_CDEMO_SK),
                nativeLong(WS_BILL_HDEMO_SK),
                nativeLong(WS_BILL_ADDR_SK),
                nativeLong(WS_SHIP_CUSTOMER_SK),
                nativeLong(WS_SHIP_CDEMO_SK),
                nativeLong(WS_SHIP_HDEMO_SK),
                nativeLong(WS_SHIP_ADDR_SK),
                nativeLong(WS_WEB_PAGE_SK),
                nativeLong(WS_WEB_SITE_SK),
                nativeLong(WS_SHIP_MODE_SK),
                nativeLong(WS_WAREHOUSE_SK),
                nativeLong(WS_PROMO_SK),
                nativeLong(WS_ORDER_NUMBER),
                new Pricing(new Decimal(nativeDecimal(WS_PRICING_WHOLESALE_COST_NUMBER), nativeInt(WS_PRICING_WHOLESALE_COST_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_LIST_PRICE_NUMBER), nativeInt(WS_PRICING_LIST_PRICE_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_SALES_PRICE_NUMBER), nativeInt(WS_PRICING_SALES_PRICE_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_EXT_DISCOUNT_AMOUNT_NUMBER), nativeInt(WS_PRICING_EXT_DISCOUNT_AMOUNT_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_EXT_SALES_PRICE_NUMBER), nativeInt(WS_PRICING_EXT_SALES_PRICE_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_EXT_WHOLESALE_COST_NUMBER), nativeInt(WS_PRICING_EXT_WHOLESALE_COST_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_EXT_LIST_PRICE_NUMBER), nativeInt(WS_PRICING_EXT_LIST_PRICE_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_COUPON_AMT_NUMBER), nativeInt(WS_PRICING_COUPON_AMT_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_NET_PAID_INC_SHIP_NUMBER), nativeInt(WS_PRICING_NET_PAID_INC_SHIP_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_NET_PAID_INC_SHIP_TAX_NUMBER), nativeInt(WS_PRICING_NET_PAID_INC_SHIP_TAX_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_NET_PROFIT_NUMBER), nativeInt(WS_PRICING_NET_PROFIT_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_NET_PAID_NUMBER), nativeInt(WS_PRICING_NET_PAID_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_NET_PAID_INC_TAX_NUMBER), nativeInt(WS_PRICING_NET_PAID_INC_TAX_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_EXT_TAX_NUMBER), nativeInt(WS_PRICING_EXT_TAX_PRECISION)),
                            new Decimal(nativeDecimal(WS_PRICING_EXT_SHIP_COST_NUMBER), nativeInt(WS_PRICING_EXT_SHIP_COST_PRECISION)),
                            nativeInt(WS_PRICING_QUANTITY)));
        restoreRowSegment();

        List<TableRow> generatedRows = new ArrayList<>(2);
        generatedRows.add(webSalesRow);
        TableRow webReturnsRow = ((WebReturnsRowNativeGenerator) childRowGenerator).generateRow(getReturnsRow());
        if (webReturnsRow != null) {
            generatedRows.add(webReturnsRow);
        }

        currPosition++;
        replaceRowWithSales();
        boolean endOfRow = nativeInt(WS_VALID) == 0;
        restoreRowSegment();
        if (endOfRow) {
            currPosition = 0;
        }
        return new RowGeneratorResult(generatedRows, endOfRow);
    }

    private void createRowLayout(RowGenerator childRowGenerator)
    {
        wrRowLayout = ((WebReturnsRowNativeGenerator) childRowGenerator).getLayout();
        rowLayout = MemoryLayout.structLayout(wsRowLayout.withName("sales"), wrRowLayout.withName("returns"));
        multipleRowsLayout = MemoryLayout.sequenceLayout(MAX_MULTIPLE_GENERATED_ROWS, rowLayout);
        allocateRow(multipleRowsLayout.byteSize());
        backupRowSegment = rowSegment;
    }

    private void replaceRowWithSales()
    {
        rowSegment = rowSegment.asSlice(currPosition * rowLayout.byteSize(), rowLayout).asSlice(0, wsRowLayout);
    }

    private void restoreRowSegment()
    {
        rowSegment = backupRowSegment;
    }

    private MemorySegment getReturnsRow()
    {
        return rowSegment.asSlice(currPosition * rowLayout.byteSize(), rowLayout).asSlice(wsRowLayout.byteSize(), wrRowLayout);
    }
}
