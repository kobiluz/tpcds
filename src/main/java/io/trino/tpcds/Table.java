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

package io.trino.tpcds;

import com.google.common.collect.ImmutableList;
import io.trino.tpcds.TableFlags.TableFlagsBuilder;
import io.trino.tpcds.column.CallCenterColumn;
import io.trino.tpcds.column.CatalogPageColumn;
import io.trino.tpcds.column.CatalogReturnsColumn;
import io.trino.tpcds.column.CatalogSalesColumn;
import io.trino.tpcds.column.Column;
import io.trino.tpcds.column.CustomerAddressColumn;
import io.trino.tpcds.column.CustomerColumn;
import io.trino.tpcds.column.CustomerDemographicsColumn;
import io.trino.tpcds.column.DateDimColumn;
import io.trino.tpcds.column.DbgenVersionColumn;
import io.trino.tpcds.column.HouseholdDemographicsColumn;
import io.trino.tpcds.column.IncomeBandColumn;
import io.trino.tpcds.column.InventoryColumn;
import io.trino.tpcds.column.ItemColumn;
import io.trino.tpcds.column.PromotionColumn;
import io.trino.tpcds.column.ReasonColumn;
import io.trino.tpcds.column.ShipModeColumn;
import io.trino.tpcds.column.StoreColumn;
import io.trino.tpcds.column.StoreReturnsColumn;
import io.trino.tpcds.column.StoreSalesColumn;
import io.trino.tpcds.column.TimeDimColumn;
import io.trino.tpcds.column.WarehouseColumn;
import io.trino.tpcds.column.WebPageColumn;
import io.trino.tpcds.column.WebReturnsColumn;
import io.trino.tpcds.column.WebSalesColumn;
import io.trino.tpcds.column.WebSiteColumn;
import io.trino.tpcds.generator.CallCenterGeneratorColumn;
import io.trino.tpcds.generator.CatalogPageGeneratorColumn;
import io.trino.tpcds.generator.CatalogReturnsGeneratorColumn;
import io.trino.tpcds.generator.CatalogSalesGeneratorColumn;
import io.trino.tpcds.generator.CustomerAddressGeneratorColumn;
import io.trino.tpcds.generator.CustomerDemographicsGeneratorColumn;
import io.trino.tpcds.generator.CustomerGeneratorColumn;
import io.trino.tpcds.generator.DateDimGeneratorColumn;
import io.trino.tpcds.generator.DbgenVersionGeneratorColumn;
import io.trino.tpcds.generator.GeneratorColumn;
import io.trino.tpcds.generator.HouseholdDemographicsGeneratorColumn;
import io.trino.tpcds.generator.IncomeBandGeneratorColumn;
import io.trino.tpcds.generator.InventoryGeneratorColumn;
import io.trino.tpcds.generator.ItemGeneratorColumn;
import io.trino.tpcds.generator.PromotionGeneratorColumn;
import io.trino.tpcds.generator.ReasonGeneratorColumn;
import io.trino.tpcds.generator.ShipModeGeneratorColumn;
import io.trino.tpcds.generator.StoreGeneratorColumn;
import io.trino.tpcds.generator.StoreReturnsGeneratorColumn;
import io.trino.tpcds.generator.StoreSalesGeneratorColumn;
import io.trino.tpcds.generator.TimeDimGeneratorColumn;
import io.trino.tpcds.generator.WarehouseGeneratorColumn;
import io.trino.tpcds.generator.WebPageGeneratorColumn;
import io.trino.tpcds.generator.WebReturnsGeneratorColumn;
import io.trino.tpcds.generator.WebSalesGeneratorColumn;
import io.trino.tpcds.generator.WebSiteGeneratorColumn;
import io.trino.tpcds.row.generator.CallCenterRowGenerator;
import io.trino.tpcds.row.generator.CallCenterRowNativeGenerator;
import io.trino.tpcds.row.generator.CatalogPageRowGenerator;
import io.trino.tpcds.row.generator.CatalogPageRowNativeGenerator;
import io.trino.tpcds.row.generator.CatalogReturnsRowGenerator;
import io.trino.tpcds.row.generator.CatalogReturnsRowNativeGenerator;
import io.trino.tpcds.row.generator.CatalogSalesRowGenerator;
import io.trino.tpcds.row.generator.CatalogSalesRowNativeGenerator;
import io.trino.tpcds.row.generator.CustomerAddressRowGenerator;
import io.trino.tpcds.row.generator.CustomerAddressRowNativeGenerator;
import io.trino.tpcds.row.generator.CustomerDemographicsRowGenerator;
import io.trino.tpcds.row.generator.CustomerDemographicsRowNativeGenerator;
import io.trino.tpcds.row.generator.CustomerRowGenerator;
import io.trino.tpcds.row.generator.CustomerRowNativeGenerator;
import io.trino.tpcds.row.generator.DateDimRowGenerator;
import io.trino.tpcds.row.generator.DateDimRowNativeGenerator;
import io.trino.tpcds.row.generator.DbgenVersionRowGenerator;
import io.trino.tpcds.row.generator.HouseholdDemographicsRowGenerator;
import io.trino.tpcds.row.generator.HouseholdDemographicsRowNativeGenerator;
import io.trino.tpcds.row.generator.IncomeBandRowGenerator;
import io.trino.tpcds.row.generator.IncomeBandRowNativeGenerator;
import io.trino.tpcds.row.generator.InventoryRowGenerator;
import io.trino.tpcds.row.generator.InventoryRowNativeGenerator;
import io.trino.tpcds.row.generator.ItemRowGenerator;
import io.trino.tpcds.row.generator.ItemRowNativeGenerator;
import io.trino.tpcds.row.generator.PromotionRowGenerator;
import io.trino.tpcds.row.generator.PromotionRowNativeGenerator;
import io.trino.tpcds.row.generator.ReasonRowGenerator;
import io.trino.tpcds.row.generator.ReasonRowNativeGenerator;
import io.trino.tpcds.row.generator.RowGenerator;
import io.trino.tpcds.row.generator.ShipModeRowGenerator;
import io.trino.tpcds.row.generator.ShipModeRowNativeGenerator;
import io.trino.tpcds.row.generator.StoreReturnsRowGenerator;
import io.trino.tpcds.row.generator.StoreRowGenerator;
import io.trino.tpcds.row.generator.StoreRowNativeGenerator;
import io.trino.tpcds.row.generator.StoreSalesRowGenerator;
import io.trino.tpcds.row.generator.TimeDimRowGenerator;
import io.trino.tpcds.row.generator.WarehouseRowGenerator;
import io.trino.tpcds.row.generator.WebPageRowGenerator;
import io.trino.tpcds.row.generator.WebReturnsRowGenerator;
import io.trino.tpcds.row.generator.WebSalesRowGenerator;
import io.trino.tpcds.row.generator.WebSiteRowGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;

public enum Table
{
    CALL_CENTER(new TableFlagsBuilder().setIsSmall().setKeepsHistory().build(),
            100,
            0xB,
            TableGenerator.isNativeGenerator() ? CallCenterRowNativeGenerator.class : CallCenterRowGenerator.class,
            CallCenterGeneratorColumn.values(),
            CallCenterColumn.values(),
            new ScalingInfo(0, new int[] {0, 3, 12, 15, 18, 21, 24, 27, 30, 30}, TableGenerator.getNativeRowCount(0))),
    CATALOG_PAGE(new TableFlagsBuilder().build(),
            200,
            0x3,
            TableGenerator.isNativeGenerator() ? CatalogPageRowNativeGenerator.class : CatalogPageRowGenerator.class,
            CatalogPageGeneratorColumn.values(),
            CatalogPageColumn.values(),
            new ScalingInfo(0, new int[] {0, 11718, 12000, 20400, 26000, 30000, 36000, 40000, 46000, 50000}, TableGenerator.getNativeRowCount(1))),
    CATALOG_RETURNS(new TableFlagsBuilder().build(),
            400,
            0x10007,
            TableGenerator.isNativeGenerator() ? CatalogReturnsRowNativeGenerator.class : CatalogReturnsRowGenerator.class,
            CatalogReturnsGeneratorColumn.values(),
            CatalogReturnsColumn.values(),
            new ScalingInfo(4, new int[] {0, 16, 160, 1600, 4800, 16000, 48000, 160000, 480000, 1600000}, TableGenerator.getNativeRowCount(2))),
    CATALOG_SALES(new TableFlagsBuilder().setIsDateBased().build(),
            100,
            0x28000,
            TableGenerator.isNativeGenerator() ? CatalogSalesRowNativeGenerator.class : CatalogSalesRowGenerator.class,
            CatalogSalesGeneratorColumn.values(),
            CatalogSalesColumn.values(),
            new ScalingInfo(4, new int[] {0, 16, 160, 1600, 4800, 16000, 48000, 160000, 480000, 1600000}, TableGenerator.getNativeRowCount(3))),
    CUSTOMER(new TableFlagsBuilder().build(),
            700,
            0x13,
            TableGenerator.isNativeGenerator() ? CustomerRowNativeGenerator.class : CustomerRowGenerator.class,
            CustomerGeneratorColumn.values(),
            CustomerColumn.values(),
            new ScalingInfo(3, new int[] {0, 100, 500, 2000, 5000, 12000, 30000, 65000, 80000, 100000}, TableGenerator.getNativeRowCount(4))),
    CUSTOMER_ADDRESS(new TableFlagsBuilder().build(),
            600,
            0x3,
            TableGenerator.isNativeGenerator() ? CustomerAddressRowNativeGenerator.class : CustomerAddressRowGenerator.class,
            CustomerAddressGeneratorColumn.values(),
            CustomerAddressColumn.values(),
            new ScalingInfo(3, new int[] {0, 50, 250, 1000, 2500, 6000, 15000, 32500, 40000, 50000}, TableGenerator.getNativeRowCount(5))),
    CUSTOMER_DEMOGRAPHICS(new TableFlagsBuilder().build(),
            0,
            0x1,
            TableGenerator.isNativeGenerator() ? CustomerDemographicsRowNativeGenerator.class : CustomerDemographicsRowGenerator.class,
            CustomerDemographicsGeneratorColumn.values(),
            CustomerDemographicsColumn.values(),
            new ScalingInfo(2, new int[] {0, 19208, 19208, 19208, 19208, 19208, 19208, 19208, 19208, 19208}, TableGenerator.getNativeRowCount(6))),
    DATE_DIM(new TableFlagsBuilder().build(),
            0,
            0x03,
            TableGenerator.isNativeGenerator() ? DateDimRowNativeGenerator.class : DateDimRowGenerator.class,
            DateDimGeneratorColumn.values(),
            DateDimColumn.values(),
            new ScalingInfo(0, new int[] {0, 73049, 73049, 73049, 73049, 73049, 73049, 73049, 73049, 73049}, TableGenerator.getNativeRowCount(7))),
    HOUSEHOLD_DEMOGRAPHICS(new TableFlagsBuilder().build(),
            0,
            0x01,
            TableGenerator.isNativeGenerator() ? HouseholdDemographicsRowNativeGenerator.class : HouseholdDemographicsRowGenerator.class,
            HouseholdDemographicsGeneratorColumn.values(),
            HouseholdDemographicsColumn.values(),
            new ScalingInfo(0, new int[] {0, 7200, 7200, 7200, 7200, 7200, 7200, 7200, 7200, 7200}, TableGenerator.getNativeRowCount(8))),
    INCOME_BAND(new TableFlagsBuilder().build(),
            0,
            0x1,
            TableGenerator.isNativeGenerator() ? IncomeBandRowNativeGenerator.class : IncomeBandRowGenerator.class,
            IncomeBandGeneratorColumn.values(),
            IncomeBandColumn.values(),
            new ScalingInfo(0, new int[] {0, 20, 20, 20, 20, 20, 20, 20, 20, 20}, TableGenerator.getNativeRowCount(9))),
    INVENTORY(new TableFlagsBuilder().setIsDateBased().build(),
            1000,
            0x07,
            TableGenerator.isNativeGenerator() ? InventoryRowNativeGenerator.class : InventoryRowGenerator.class,
            InventoryGeneratorColumn.values(),
            InventoryColumn.values(),
            new ScalingInfo(0, new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, TableGenerator.getNativeRowCount(10))), // the inventory table is scaled based on item and warehouse
    ITEM(new TableFlagsBuilder().setKeepsHistory().build(),
            50,
            0x0B,
            TableGenerator.isNativeGenerator() ? ItemRowNativeGenerator.class : ItemRowGenerator.class,
            ItemGeneratorColumn.values(),
            ItemColumn.values(),
            new ScalingInfo(3, new int[] {0, 9, 51, 102, 132, 150, 180, 201, 231, 251}, TableGenerator.getNativeRowCount(11))),
    PROMOTION(new TableFlagsBuilder().build(),
            200,
            0x3,
            TableGenerator.isNativeGenerator() ? PromotionRowNativeGenerator.class : PromotionRowGenerator.class,
            PromotionGeneratorColumn.values(),
            PromotionColumn.values(),
            new ScalingInfo(0, new int[] {0, 300, 500, 1000, 1300, 1500, 1800, 2000, 2300, 2500}, TableGenerator.getNativeRowCount(12))),
    REASON(new TableFlagsBuilder().build(),
            0,
            0x03,
            TableGenerator.isNativeGenerator() ? ReasonRowNativeGenerator.class : ReasonRowGenerator.class,
            ReasonGeneratorColumn.values(),
            ReasonColumn.values(),
            new ScalingInfo(0, new int[] {0, 35, 45, 55, 60, 65, 67, 70, 72, 75}, TableGenerator.getNativeRowCount(13))),
    SHIP_MODE(new TableFlagsBuilder().build(),
            0,
            0x03,
            TableGenerator.isNativeGenerator() ? ShipModeRowNativeGenerator.class : ShipModeRowGenerator.class,
            ShipModeGeneratorColumn.values(),
            ShipModeColumn.values(),
            new ScalingInfo(0, new int[] {0, 20, 20, 20, 20, 20, 20, 20, 20, 20}, TableGenerator.getNativeRowCount(14))),
    STORE(new TableFlagsBuilder().setKeepsHistory().setIsSmall().build(),
            100,
            0xB,
            TableGenerator.isNativeGenerator() ? StoreRowNativeGenerator.class : StoreRowGenerator.class,
            StoreGeneratorColumn.values(),
            StoreColumn.values(),
            new ScalingInfo(0, new int[] {0, 6, 51, 201, 402, 501, 675, 750, 852, 951}, TableGenerator.getNativeRowCount(15))),
    STORE_RETURNS(new TableFlagsBuilder().build(),
            700,
            0x204,
            StoreReturnsRowGenerator.class,
            StoreReturnsGeneratorColumn.values(),
            StoreReturnsColumn.values(),
            new ScalingInfo(4, new int[] {0, 24, 240, 2400, 7200, 24000, 72000, 240000, 720000, 2400000}, 0)),
    STORE_SALES(new TableFlagsBuilder().setIsDateBased().build(),
            900,
            0x204,
            StoreSalesRowGenerator.class,
            StoreSalesGeneratorColumn.values(),
            StoreSalesColumn.values(),
            new ScalingInfo(4, new int[] {0, 24, 240, 2400, 7200, 24000, 72000, 240000, 720000, 2400000}, 0)),
    TIME_DIM(new TableFlagsBuilder().build(),
            0,
            0x03,
            TimeDimRowGenerator.class,
            TimeDimGeneratorColumn.values(),
            TimeDimColumn.values(),
            new ScalingInfo(0, new int[] {0, 86400, 86400, 86400, 86400, 86400, 86400, 86400, 86400, 86400}, 0)),
    WAREHOUSE(new TableFlagsBuilder().setIsSmall().build(),
            200,
            0x03,
            WarehouseRowGenerator.class,
            WarehouseGeneratorColumn.values(),
            WarehouseColumn.values(),
            new ScalingInfo(0, new int[] {0, 5, 10, 15, 17, 20, 22, 25, 27, 30}, 0)),
    WEB_PAGE(new TableFlagsBuilder().setKeepsHistory().build(),
            250,
            0x0B,
            WebPageRowGenerator.class,
            WebPageGeneratorColumn.values(),
            WebPageColumn.values(),
            new ScalingInfo(0, new int[] {0, 30, 100, 1020, 1302, 1500, 1800, 2001, 2301, 2502}, 0)),
    WEB_RETURNS(new TableFlagsBuilder().build(),
            900,
            0x2004,
            WebReturnsRowGenerator.class,
            WebReturnsGeneratorColumn.values(),
            WebReturnsColumn.values(),
            new ScalingInfo(3, new int[] {0, 60, 600, 6000, 18000, 60000, 180000, 600000, 1800000, 6000000}, 0)),
    WEB_SALES(new TableFlagsBuilder().setIsDateBased().build(),
            5,
            0x20008,
            WebSalesRowGenerator.class,
            WebSalesGeneratorColumn.values(),
            WebSalesColumn.values(),
            new ScalingInfo(3, new int[] {0, 60, 600, 6000, 18000, 60000, 180000, 600000, 1800000, 6000000}, 0)),
    WEB_SITE(new TableFlagsBuilder().setKeepsHistory().setIsSmall().build(),
            100,
            0x0B,
            WebSiteRowGenerator.class,
            WebSiteGeneratorColumn.values(),
            WebSiteColumn.values(),
            new ScalingInfo(0, new int[] {0, 15, 21, 12, 21, 27, 33, 39, 42, 48}, 0)),
    DBGEN_VERSION(new TableFlagsBuilder().build(),
            0,
            0x0,
            DbgenVersionRowGenerator.class,
            DbgenVersionGeneratorColumn.values(),
            DbgenVersionColumn.values(),
            new ScalingInfo(0, new int[] {0, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 0));

    private final TableFlags tableFlags;
    private final int nullBasisPoints;
    private final long notNullBitMap;
    private final Class<? extends RowGenerator> rowGeneratorClass;
    private final GeneratorColumn[] generatorColumns;
    private final Column[] columns;
    private final ScalingInfo scalingInfo;
    private Optional<Table> parent = Optional.empty();
    private Optional<Table> child = Optional.empty();

    public Class<? extends RowGenerator> getRowGeneratorClass()
    {
        return rowGeneratorClass;
    }

    static {
        // initialize parent and child relationships here because in
        // table constructors can't refer to tables that have not yet been
        // defined
        CATALOG_RETURNS.parent = Optional.of(CATALOG_SALES);
        CATALOG_SALES.child = Optional.of(CATALOG_RETURNS);
        STORE_RETURNS.parent = Optional.of(STORE_SALES);
        STORE_SALES.child = Optional.of(STORE_RETURNS);
        WEB_RETURNS.parent = Optional.of(WEB_SALES);
        WEB_SALES.child = Optional.of(WEB_RETURNS);
    }

    Table(TableFlags tableFlags, int nullBasisPoints, long notNullBitMap, Class<? extends RowGenerator> rowGeneratorClass, GeneratorColumn[] generatorColumns, Column[] columns, ScalingInfo scalingInfo)
    {
        this.tableFlags = tableFlags;
        this.nullBasisPoints = nullBasisPoints;
        this.notNullBitMap = notNullBitMap;
        this.rowGeneratorClass = rowGeneratorClass;
        this.generatorColumns = generatorColumns;
        this.columns = columns;
        this.scalingInfo = scalingInfo;
    }

    public String getName()
    {
        return name().toLowerCase();
    }

    public boolean keepsHistory()
    {
        return tableFlags.keepsHistory();
    }

    public boolean isSmall()
    {
        return tableFlags.isSmall();
    }

    public boolean hasChild()
    {
        return child.isPresent();
    }

    public Table getChild()
    {
        return child.get();
    }

    public boolean isChild()
    {
        return parent.isPresent();
    }

    public Table getParent()
    {
        return parent.get();
    }

    public int getNullBasisPoints()
    {
        return nullBasisPoints;
    }

    public long getNotNullBitMap()
    {
        return notNullBitMap;
    }

    public GeneratorColumn[] getGeneratorColumns()
    {
        return generatorColumns;
    }

    public ScalingInfo getScalingInfo()
    {
        return scalingInfo;
    }

    public Column[] getColumns()
    {
        return columns;
    }

    public Column getColumn(String columnName)
    {
        List<Column> allColumnMatches = Arrays.stream(getColumns())
                .filter(column -> columnName.toLowerCase().equals(column.getName().toLowerCase()))
                .collect(Collectors.toList());

        checkState(allColumnMatches.size() == 1);
        return allColumnMatches.get(0);
    }

    public static Table getTable(String tableName)
    {
        List<Table> allTableMatches = getBaseTables().stream()
                .filter(table -> tableName.toLowerCase().equals(table.getName().toLowerCase()))
                .collect(Collectors.toList());

        checkState(allTableMatches.size() == 1);
        return allTableMatches.get(0);
    }

    public static List<Table> getBaseTables()
    {
        List<Table> allTables = ImmutableList.copyOf(Table.values());
        return allTables.stream()
                .filter(table -> !table.getName().startsWith("s_"))
                .collect(Collectors.toList());
    }
}
