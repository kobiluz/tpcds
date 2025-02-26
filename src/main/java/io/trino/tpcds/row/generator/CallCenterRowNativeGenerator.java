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
import io.trino.tpcds.row.CallCenterRow;

import javax.annotation.concurrent.NotThreadSafe;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemoryLayout.PathElement;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SequenceLayout;
import java.lang.foreign.StructLayout;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;

import static io.trino.tpcds.Nulls.createNullBitMap;
import static io.trino.tpcds.Table.CALL_CENTER;
import static io.trino.tpcds.TableGenerator.nativeGeneratorLookup;
import static io.trino.tpcds.column.CallCenterColumn.CC_CALL_CENTER_ID;
import static io.trino.tpcds.column.CallCenterColumn.CC_CALL_CENTER_SK;
import static io.trino.tpcds.column.CallCenterColumn.CC_CITY;
import static io.trino.tpcds.column.CallCenterColumn.CC_CLASS;
import static io.trino.tpcds.column.CallCenterColumn.CC_CLOSED_DATE_SK;
import static io.trino.tpcds.column.CallCenterColumn.CC_COMPANY;
import static io.trino.tpcds.column.CallCenterColumn.CC_COMPANY_NAME;
import static io.trino.tpcds.column.CallCenterColumn.CC_COUNTRY;
import static io.trino.tpcds.column.CallCenterColumn.CC_COUNTY;
import static io.trino.tpcds.column.CallCenterColumn.CC_DIVISION;
import static io.trino.tpcds.column.CallCenterColumn.CC_DIVISION_NAME;
import static io.trino.tpcds.column.CallCenterColumn.CC_EMPLOYEES;
import static io.trino.tpcds.column.CallCenterColumn.CC_GMT_OFFSET;
import static io.trino.tpcds.column.CallCenterColumn.CC_HOURS;
import static io.trino.tpcds.column.CallCenterColumn.CC_MANAGER;
import static io.trino.tpcds.column.CallCenterColumn.CC_MARKET_MANAGER;
import static io.trino.tpcds.column.CallCenterColumn.CC_MKT_CLASS;
import static io.trino.tpcds.column.CallCenterColumn.CC_MKT_DESC;
import static io.trino.tpcds.column.CallCenterColumn.CC_MKT_ID;
import static io.trino.tpcds.column.CallCenterColumn.CC_NAME;
import static io.trino.tpcds.column.CallCenterColumn.CC_OPEN_DATE_SK;
import static io.trino.tpcds.column.CallCenterColumn.CC_REC_END_DATE;
import static io.trino.tpcds.column.CallCenterColumn.CC_REC_START_DATE;
import static io.trino.tpcds.column.CallCenterColumn.CC_SQ_FT;
import static io.trino.tpcds.column.CallCenterColumn.CC_STATE;
import static io.trino.tpcds.column.CallCenterColumn.CC_STREET_NAME;
import static io.trino.tpcds.column.CallCenterColumn.CC_STREET_NUMBER;
import static io.trino.tpcds.column.CallCenterColumn.CC_STREET_TYPE;
import static io.trino.tpcds.column.CallCenterColumn.CC_SUITE_NUMBER;
import static io.trino.tpcds.column.CallCenterColumn.CC_TAX_PERCENTAGE;
import static io.trino.tpcds.column.CallCenterColumn.CC_ZIP;
import static io.trino.tpcds.generator.CallCenterGeneratorColumn.CC_NULLS;
import static io.trino.tpcds.type.Address;
import static io.trino.tpcds.type.Address.AddressBuilder;
import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_BYTE;
import static java.lang.foreign.ValueLayout.JAVA_INT;
import static java.lang.foreign.ValueLayout.JAVA_LONG;

@NotThreadSafe
public class CallCenterRowNativeGenerator
        extends AbstractRowGenerator
{
    private final SequenceLayout aSuiteNumberLayout;
    private final SequenceLayout aCountryLayout;
    private final StructLayout aLayout;

    private final StructLayout dLayout;

    private final SequenceLayout ccCallCenterIdLayout;
    private final SequenceLayout ccNameLayout;
    private final SequenceLayout ccManagerLayout;
    private final SequenceLayout ccMarketClassLayout;
    private final SequenceLayout ccMarketDescLayout;
    private final SequenceLayout ccMarketManagerLayout;
    private final SequenceLayout ccDivisionNameLayout;
    private final SequenceLayout ccCompanyNameLayout;
    private final StructLayout ccRowLayout;

    private final long ccCallCenterSkOffset;
    private final long ccCallCenterIdOffset;
    private final long ccRecStartDateOffset;
    private final long ccRecEndDateOffset;
    private final long ccClosedDateOffset;
    private final long ccOpenDateOffset;
    private final long ccNameOffset;
    private final long ccClassOffset;
    private final long ccEmployeesOffset;
    private final long ccSqFtOffset;
    private final long ccHoursOffset;
    private final long ccManagerOffset;
    private final long ccMarketIdOffset;
    private final long ccMarketClassOffset;
    private final long ccMarketDescOffset;
    private final long ccMarketManagerOffset;
    private final long ccDivisionOffset;
    private final long ccDivisionNameOffset;
    private final long ccCompanyOffset;
    private final long ccCompanyNameOffset;
    private final long aOffset;
    private final long aSuiteNumberOffset;
    private final long aStreetNumberOffset;
    private final long aStreetNameOffset;
    private final long aStreetName2Offset;
    private final long aStreetTypeOffset;
    private final long aCityOffset;
    private final long aCountyOffset;
    private final long aStateOffset;
    private final long aCountryOffset;
    private final long aZipOffset;
    private final long aGmtOffsetOffset;
    private final long dTaxPrecentageOffset;

    private final MethodHandle ccMakeRow;

    public CallCenterRowNativeGenerator()
    {
        super(CALL_CENTER);

        try {
            SymbolLookup nativeGeneratorLookup = nativeGeneratorLookup();
            ccMakeRow = Linker.nativeLinker().downcallHandle(nativeGeneratorLookup.find("mk_w_call_center").orElseThrow(), FunctionDescriptor.of(JAVA_INT, ADDRESS, JAVA_LONG));
            System.out.println("got method");
            aSuiteNumberLayout = MemoryLayout.sequenceLayout(CC_SUITE_NUMBER.getType().getPrecision().get() + 6, JAVA_BYTE);
            aCountryLayout = MemoryLayout.sequenceLayout(CC_COUNTRY.getType().getPrecision().get() + 4, JAVA_BYTE);
            aLayout = MemoryLayout.structLayout(
                    aSuiteNumberLayout.withName(CC_SUITE_NUMBER.getName()),
                    ADDRESS.withName(CC_STREET_NAME.getName()),
                    ADDRESS.withName(CC_STREET_NAME.getName() + "2"),
                    ADDRESS.withName(CC_STREET_TYPE.getName()),
                    ADDRESS.withName(CC_CITY.getName()),
                    ADDRESS.withName(CC_COUNTY.getName()),
                    ADDRESS.withName(CC_STATE.getName()),
                    aCountryLayout.withName(CC_COUNTRY.getName()),
                    JAVA_INT.withName(CC_STREET_NUMBER.getName()),
                    JAVA_INT.withName(CC_ZIP.getName()),
                    JAVA_INT.withName("dummy"),
                    JAVA_INT.withName(CC_GMT_OFFSET.getName()));
            System.out.println("address layout");
            dLayout = MemoryLayout.structLayout(
                    JAVA_LONG.withName("number"),
                    JAVA_INT.withName("flags"),
                    JAVA_INT.withName("precision"),
                    JAVA_INT.withName("scale"));
            System.out.println("decimal layout");
            ccCallCenterIdLayout = MemoryLayout.sequenceLayout(CC_CALL_CENTER_ID.getType().getPrecision().get() + 8, JAVA_BYTE);
            ccNameLayout = MemoryLayout.sequenceLayout(CC_NAME.getType().getPrecision().get() + 6, JAVA_BYTE);
            ccManagerLayout = MemoryLayout.sequenceLayout(CC_MANAGER.getType().getPrecision().get() + 8, JAVA_BYTE);
            ccMarketClassLayout = MemoryLayout.sequenceLayout(CC_MKT_CLASS.getType().getPrecision().get() + 6, JAVA_BYTE);
            ccMarketDescLayout = MemoryLayout.sequenceLayout(CC_MKT_DESC.getType().getPrecision().get() + 4, JAVA_BYTE);
            ccMarketManagerLayout = MemoryLayout.sequenceLayout(CC_MARKET_MANAGER.getType().getPrecision().get() + 8, JAVA_BYTE);
            ccDivisionNameLayout = MemoryLayout.sequenceLayout(CC_DIVISION_NAME.getType().getPrecision().get() + 6, JAVA_BYTE);
            ccCompanyNameLayout = MemoryLayout.sequenceLayout(CC_COMPANY_NAME.getType().getPrecision().get() + 14, JAVA_BYTE);
            ccRowLayout = MemoryLayout.structLayout(
                    JAVA_LONG.withName(CC_CALL_CENTER_SK.getName()),
                    ccCallCenterIdLayout.withName(CC_CALL_CENTER_ID.getName()),
                    JAVA_LONG.withName(CC_REC_START_DATE.getName()),
                    JAVA_LONG.withName(CC_REC_END_DATE.getName()),
                    JAVA_LONG.withName(CC_CLOSED_DATE_SK.getName()),
                    JAVA_LONG.withName(CC_OPEN_DATE_SK.getName()),
                    ccNameLayout.withName(CC_NAME.getName()),
                    ADDRESS.withName(CC_CLASS.getName()),
                    JAVA_INT.withName(CC_EMPLOYEES.getName()),
                    JAVA_INT.withName(CC_SQ_FT.getName()),
                    ADDRESS.withName(CC_HOURS.getName()),
                    ccManagerLayout.withName(CC_MANAGER.getName()),
                    JAVA_INT.withName(CC_MKT_ID.getName()),
                    ccMarketClassLayout.withName(CC_MKT_CLASS.getName()),
                    ccMarketDescLayout.withName(CC_MKT_DESC.getName()),
                    ccMarketManagerLayout.withName(CC_MARKET_MANAGER.getName()),
                    JAVA_INT.withName(CC_DIVISION.getName()),
                    ccDivisionNameLayout.withName(CC_DIVISION_NAME.getName()),
                    JAVA_INT.withName(CC_COMPANY.getName()),
                    ccCompanyNameLayout.withName(CC_COMPANY_NAME.getName()),
                    JAVA_INT.withName("padding"),
                    aLayout.withName("address"),
                    dLayout.withName(CC_TAX_PERCENTAGE.getName()));
            System.out.println("row layout");
            ccCallCenterSkOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_CALL_CENTER_SK.getName()));
            ccCallCenterIdOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_CALL_CENTER_ID.getName()));
            ccRecStartDateOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_REC_START_DATE.getName()));
            ccRecEndDateOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_REC_END_DATE.getName()));
            ccClosedDateOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_CLOSED_DATE_SK.getName()));
            ccOpenDateOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_OPEN_DATE_SK.getName()));
            ccNameOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_NAME.getName()));
            ccClassOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_CLASS.getName()));
            ccEmployeesOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_EMPLOYEES.getName()));
            ccSqFtOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_SQ_FT.getName()));
            ccHoursOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_HOURS.getName()));
            ccManagerOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_MANAGER.getName()));
            ccMarketIdOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_MKT_ID.getName()));
            ccMarketClassOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_MKT_CLASS.getName()));
            ccMarketDescOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_MKT_DESC.getName()));
            ccMarketManagerOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_MARKET_MANAGER.getName()));
            ccDivisionOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_DIVISION.getName()));
            ccDivisionNameOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_DIVISION_NAME.getName()));
            ccCompanyOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_COMPANY.getName()));
            ccCompanyNameOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_COMPANY_NAME.getName()));
            aOffset = ccRowLayout.byteOffset(PathElement.groupElement("address"));
            aSuiteNumberOffset = aOffset + aLayout.byteOffset(PathElement.groupElement(CC_SUITE_NUMBER.getName()));
            aStreetNumberOffset = aOffset + aLayout.byteOffset(PathElement.groupElement(CC_STREET_NUMBER.getName()));
            aStreetNameOffset = aOffset + aLayout.byteOffset(PathElement.groupElement(CC_STREET_NAME.getName()));
            aStreetName2Offset = aOffset + aLayout.byteOffset(PathElement.groupElement(CC_STREET_NAME.getName() + "2"));
            aStreetTypeOffset = aOffset + aLayout.byteOffset(PathElement.groupElement(CC_STREET_TYPE.getName()));
            aCityOffset = aOffset + aLayout.byteOffset(PathElement.groupElement(CC_CITY.getName()));
            aCountyOffset = aOffset + aLayout.byteOffset(PathElement.groupElement(CC_COUNTY.getName()));
            aStateOffset = aOffset + aLayout.byteOffset(PathElement.groupElement(CC_STATE.getName()));
            aCountryOffset = aOffset + aLayout.byteOffset(PathElement.groupElement(CC_COUNTRY.getName()));
            aZipOffset = aOffset + aLayout.byteOffset(PathElement.groupElement(CC_ZIP.getName()));
            aGmtOffsetOffset = aOffset + aLayout.byteOffset(PathElement.groupElement(CC_GMT_OFFSET.getName()));
            dTaxPrecentageOffset = ccRowLayout.byteOffset(PathElement.groupElement(CC_TAX_PERCENTAGE.getName()));
            System.out.println("offsets");
        }
        catch (Throwable t) {
            System.err.println("CallCenterRowNativeGenerator failed " + t);
            throw new RuntimeException("failed to find call center row generator method", t);
        }
    }

    @Override
    public RowGeneratorResult generateRowAndChildRows(long rowNumber, Session session, RowGenerator parentRowGenerator, RowGenerator childRowGenerator)
    {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment ccRowSegment = arena.allocate(ccRowLayout.byteSize(), JAVA_LONG.byteSize());

            long ccClassLength = (long) CC_CLASS.getType().getPrecision().get();
            MemorySegment ccClassSegment = arena.allocate(ccClassLength, JAVA_INT.byteSize());
            ccRowSegment.set(ADDRESS, ccClassOffset, ccClassSegment);
            long ccHoursLength = (long) CC_HOURS.getType().getPrecision().get();
            MemorySegment ccHoursSegment = arena.allocate(ccHoursLength, JAVA_INT.byteSize());
            ccRowSegment.set(ADDRESS, ccHoursOffset, ccHoursSegment);

            long aStreetNameLength = (long) CC_STREET_NAME.getType().getPrecision().get();
            MemorySegment aStreetNameSegment = arena.allocate(aStreetNameLength, JAVA_INT.byteSize());
            MemorySegment aStreetName2Segment = arena.allocate(aStreetNameLength, JAVA_INT.byteSize());
            ccRowSegment.set(ADDRESS, aStreetNameOffset, aStreetNameSegment);
            ccRowSegment.set(ADDRESS, aStreetName2Offset, aStreetName2Segment);
            long aStreetTypeLength = (long) CC_STREET_TYPE.getType().getPrecision().get();
            MemorySegment aStreetTypeSegment = arena.allocate(aStreetTypeLength, JAVA_INT.byteSize());
            ccRowSegment.set(ADDRESS, aStreetTypeOffset, aStreetTypeSegment);
            long aCityLength = (long) CC_CITY.getType().getPrecision().get();
            MemorySegment aCitySegment = arena.allocate(aCityLength, JAVA_INT.byteSize());
            ccRowSegment.set(ADDRESS, aCityOffset, aCitySegment);
            long aCountyLength = (long) CC_COUNTY.getType().getPrecision().get();
            MemorySegment aCountySegment = arena.allocate(aCountyLength, JAVA_INT.byteSize());
            ccRowSegment.set(ADDRESS, aCountyOffset, aCountySegment);
            long aStateLength = (long) CC_STATE.getType().getPrecision().get();
            MemorySegment aStateSegment = arena.allocate(aStateLength, JAVA_INT.byteSize());
            ccRowSegment.set(ADDRESS, aStateOffset, aStateSegment);

            int res = (int) ccMakeRow.invokeExact(ccRowSegment, rowNumber);
            if (res < 0) {
                throw new RuntimeException("make row for call center table failed no error " + res);
            }

            CallCenterRow.Builder builder = new CallCenterRow.Builder();
            builder.setNullBitMap(createNullBitMap(CALL_CENTER, getRandomNumberStream(CC_NULLS)));
            builder.setCcCallCenterSk(ccRowSegment.get(JAVA_LONG, ccCallCenterSkOffset));
            builder.setCcCallCenterId(ccRowSegment.asSlice(ccCallCenterIdOffset, ccCallCenterIdLayout).getString(0));
            builder.setCcRecStartDateId(ccRowSegment.get(JAVA_LONG, ccRecStartDateOffset));
            builder.setCcRecEndDateId(ccRowSegment.get(JAVA_LONG, ccRecEndDateOffset));
            builder.setCcClosedDateId(ccRowSegment.get(JAVA_LONG, ccClosedDateOffset));
            builder.setCcOpenDateId(ccRowSegment.get(JAVA_LONG, ccOpenDateOffset));
            builder.setCcName(ccRowSegment.asSlice(ccNameOffset, ccNameLayout).getString(0));
            builder.setCcClass(ccRowSegment.get(ADDRESS, ccClassOffset).reinterpret(ccClassLength).getString(0));
            builder.setCcEmployees(ccRowSegment.get(JAVA_INT, ccEmployeesOffset));
            builder.setCcSqFt(ccRowSegment.get(JAVA_INT, ccSqFtOffset));
            builder.setCcHours(ccRowSegment.get(ADDRESS, ccHoursOffset).reinterpret(ccHoursLength).getString(0));
            builder.setCcManager(ccRowSegment.asSlice(ccManagerOffset, ccManagerLayout).getString(0));
            builder.setCcMarketId(ccRowSegment.get(JAVA_INT, ccMarketIdOffset));
            builder.setCcMarketClass(ccRowSegment.asSlice(ccMarketClassOffset, ccMarketClassLayout).getString(0));
            builder.setCcMarketDesc(ccRowSegment.asSlice(ccMarketDescOffset, ccMarketDescLayout).getString(0));
            builder.setCcMarketManager(ccRowSegment.asSlice(ccMarketManagerOffset, ccMarketManagerLayout).getString(0));
            builder.setCcDivisionId(ccRowSegment.get(JAVA_INT, ccDivisionOffset));
            builder.setCcDivisionName(ccRowSegment.asSlice(ccDivisionNameOffset, ccDivisionNameLayout).getString(0));
            builder.setCcCompany(ccRowSegment.get(JAVA_INT, ccCompanyOffset));
            builder.setCcCompanyName(ccRowSegment.asSlice(ccCompanyNameOffset, ccCompanyNameLayout).getString(0));

            //AddressBuilder addressBuilder = new AddressBuilder();
            //builder.setSuiteNumber(ccRowSegment.asSlice(aSuiteNumberOffset, aSuiteNumberLayou).getString(0));
            //builder.setStreetName1(ccRowSegment.get(ADDRESS, aStreetNameOffset).reinterpret(ccClassLength).getString(0));
            //builder.setStreetName2(pickRandomStreetName(HALF_EMPTY, randomNumberStream));
            //builder.setStreetType(pickRandomStreetType(randomNumberStream));
            //builder.setCity(city);
            //builder.setCounty(getCountyAtIndex(regionNumber));
            //builder.setState(getStateAbbreviationAtIndex(regionNumber));
            //builder.setCountry("United States");
            //builder.setStreetNumber(generateUniformRandomInt(1, 1000, randomNumberStream));
            //builder.setZip(zip + (zipPrefix) * 10000);
            //builder.setGmtOffset(getGmtOffsetAtIndex(regionNumber));
            //ADDRESS.withName(CC_STREET_NAME.getName()),
            //ADDRESS.withName(CC_STREET_NAME.getName() + "2"),
            //ADDRESS.withName(CC_STREET_TYPE.getName()),
            //ADDRESS.withName(CC_CITY.getName()),
            //ADDRESS.withName(CC_COUNTY.getName()),
            //ADDRESS.withName(CC_STATE.getName()),
            //aCountryLayout.withName(CC_COUNTRY.getName()),
            //JAVA_INT.withName(CC_STREET_NUMBER.getName()),
            //JAVA_INT.withName(CC_ZIP.getName()),
            //JAVA_INT.withName("dummy"),
            //JAVA_INT.withName(CC_GMT_OFFSET.getName()));

            //builder.setCcAddress(makeAddressForColumn(CALL_CENTER, getRandomNumberStream(CC_ADDRESS), 1)); //@TODO kobi fix
            //builder.setCcTaxPercentage(ccTaxPercentage); // @TODO kobi fix
            return new RowGeneratorResult(builder.build());
        }
        catch (Throwable t) {
            throw new RuntimeException("failed to invoke call center row generator method", t);
        }
    }
}
